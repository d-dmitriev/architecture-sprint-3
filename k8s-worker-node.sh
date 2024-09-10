#!/bin/bash

sudo hostname ubuntu-worker

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
sudo DEBIAN_FRONTEND=noninteractive DEBCONF_NOWARNINGS=yes apt-get install -y -qq kubelet kubeadm > /dev/null
sudo apt-mark hold kubelet kubeadm > /dev/null

echo "[kubeadm/join] Join to kubernetes cluster..."
sudo kubeadm join $K8S_MASTER --token $K8S_TOKEN \
	--discovery-token-ca-cert-hash $K8S_TOKEN_CA_CERT_HASH