#!/bin/bash

minikube start --cpus='6' --nodes='3'

istioctl install -f ./tracing.yaml -y

kubectl apply -f ./kiali.yaml
kubectl apply -f ./grafana.yaml
kubectl apply -f ./prometheus.yaml
kubectl apply -f ./jaeger.yaml

cd terraform
terraform apply -auto-approve
cd ..

kubectl port-forward svc/istio-ingressgateway -n istio-system 8080:80
