#!/bin/sh
echo Starting VScan-API version: $VSCAN_API_VER
rm -rf /home/centos/egar-vscan-api/scripts/kube/vscan-api-deployment.yaml; envsubst < "/home/centos/egar-vscan-api/scripts/kube/vscan-api-deployment-template.yaml" > "/home/centos/egar-vscan-api/scripts/kube/vscan-api-deployment.yaml"
kubectl create -f /home/centos/egar-vscan-api/scripts/kube/vscan-api-deployment.yaml
kubectl create -f /home/centos/egar-vscan-api/scripts/kube/vscan-api-service.yaml

