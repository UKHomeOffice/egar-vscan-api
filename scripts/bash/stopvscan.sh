#!/bin/sh
kubectl delete -f /home/centos/egar-vscan-api/scripts/kube/vscan-api-deployment.yaml
kubectl delete -f /home/centos/egar-vscan-api/scripts/kube/vscan-api-service.yaml

