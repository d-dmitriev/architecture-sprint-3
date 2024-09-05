#!/bin/bash

minikube start --nodes=3
# sleep 10
kubectl apply -f ./test-nodes.yaml
minikube addons enable ingress
minikube addons enable metrics-server

kubectl port-forward service/ingress-nginx-controller -n ingress-nginx 8080:80