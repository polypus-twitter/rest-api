#!/bin/bash
tar czf rest-api.tar.gz src env.sh polypus-api.conf pom.xml spark-launcher.sh LICENSE
docker build -t polypus/rest-api .
rm rest-api.tar.gz
