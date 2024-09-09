#!/bin/bash

sudo hostname ubuntu-master
echo "[apt/install] Install dependencies..."
sudo swapoff -a
sudo apt-get update > /dev/null
sudo DEBIAN_FRONTEND=noninteractive DEBCONF_NOWARNINGS=yes apt-get install -y -qq apt-transport-https ca-certificates curl gpg > /dev/null
echo "[apt/install] Install containerd..."
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update > /dev/null
sudo DEBIAN_FRONTEND=noninteractive DEBCONF_NOWARNINGS=yes apt-get install -y -qq containerd.io > /dev/null
echo "[bash/init] Configure containerd..."
sudo containerd config default | sudo tee /etc/containerd/config.toml > /dev/null
sudo sed -i 's/SystemdCgroup = false/SystemdCgroup = true/' /etc/containerd/config.toml
sudo systemctl restart containerd
cat <<EOF | sudo tee /etc/modules-load.d/containerd.conf > /dev/null
overlay
br_netfilter
EOF
sudo modprobe overlay
sudo modprobe br_netfilter
cat <<EOF | sudo tee /etc/sysctl.d/99-kubernetes-cri.conf > /dev/null
net.bridge.bridge-nf-call-iptables  = 1
net.ipv4.ip_forward                 = 1
net.bridge.bridge-nf-call-ip6tables = 1
EOF
sudo sysctl --system > /dev/null
echo "[apt/install] Install kubernetes..."
curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.30/deb/Release.key | sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg
echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.30/deb/ /' | sudo tee /etc/apt/sources.list.d/kubernetes.list > /dev/null
sudo apt-get update > /dev/null
sudo DEBIAN_FRONTEND=noninteractive DEBCONF_NOWARNINGS=yes apt-get install -y -qq kubelet kubeadm kubectl > /dev/null
sudo apt-mark hold kubelet kubeadm kubectl > /dev/null
sudo systemctl enable --now kubelet
echo "[kubeadm/init] Init kubernetes..."
sudo kubeadm config images pull
sudo kubeadm init --pod-network-cidr=10.1.0.0/16 --service-cidr=10.2.0.0/16 --upload-certs > /dev/null
echo "[bash/init] Prepare kubectl..."
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
echo "[kubectl/patch] Enable shedule..."
kubectl patch node ubuntu-master -p "{\"spec\":{\"unschedulable\":false}}" > /dev/null
kubectl taint nodes ubuntu-master node-role.kubernetes.io/control-plane=true:NoSchedule-  > /dev/null
echo "[kubectl/create] Install flannel..."
wget -q https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
sed -i 's/vxlan/host-gw/' kube-flannel.yml
sed -i 's#10.244.0.0/16#10.1.0.0/16#' kube-flannel.yml
kubectl create -f kube-flannel.yml > /dev/null
echo "[kubectl/wait] Wait nodes..."
kubectl wait --for=condition=Ready nodes --all --timeout=600s > /dev/null
echo "[kubectl/apply] Install ingress nginx..."
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.11.2/deploy/static/provider/baremetal/deploy.yaml --wait > /dev/null
kubectl patch svc ingress-nginx-controller  -n ingress-nginx -p '{"spec": {"type": "LoadBalancer", "externalIPs":["192.168.1.169"]}}' > /dev/null
echo "[kubectl/wait] Wait ingress nginx..."
kubectl wait --for=condition=Ready pods -l app.kubernetes.io/component=controller -n ingress-nginx --timeout=600s > /dev/null
echo "[kubectl/apply] Install demo app..."
cat <<EOF | kubectl apply --wait -f - > /dev/null
kind: Deployment
apiVersion: apps/v1
metadata:
  name: foo-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: foo
  template:
    metadata:
      labels:
        app: foo
    spec:
      containers:
        - name: foo-app
          image: 'kicbase/echo-server:1.0'
          ports:
          - containerPort: 8080
EOF

cat <<EOF | kubectl apply --wait -f - > /dev/null
kind: Service
apiVersion: v1
metadata:
  name: foo-service
spec:
  type: ClusterIP
  selector:
    app: foo
  ports:
    - port: 8080
      targetPort: 8080
EOF

cat <<EOF | kubectl apply --wait -f - > /dev/null
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: ingress-web
spec:
  ingressClassName: nginx
  rules:
  - host: foo.k8s.local
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: foo-service
            port:
              number: 8080
EOF

kubectl get no,ing,po,svc