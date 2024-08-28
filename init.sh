#!/bin/bash

minikube start --cpus='6'
kubectl create secret docker-registry ghcr --docker-server=https://ghcr.io --docker-username=d-dmitriev --docker-password=${GITHUB_TOKEN} -n default
# istioctl install -y
istioctl install -f ./tracing.yaml -y
# kusk cluster install
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.23/samples/addons/kiali.yaml
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.23/samples/addons/grafana.yaml
# kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.23/samples/addons/prometheus.yaml
kubectl apply -f ./prometheus.yaml
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.23/samples/addons/jaeger.yaml
cd terraform
terraform apply -auto-approve
cd ..
# kusk deploy -i api.yaml 
# kubectl port-forward svc/kusk-gateway-envoy-fleet -n kusk-system 8080:80
#kubectl port-forward svc/smart-home-monolith -n default 8080:8080
#minikube delete
kubectl port-forward svc/istio-ingressgateway -n istio-system 8080:80
