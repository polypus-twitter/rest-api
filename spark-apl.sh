#!/bin/bash
source ./apl-env.sh
echo NUM_EXECUTORS=$NUM_EXECUTORS
echo EXECUTOR_CORES=$EXECUTOR_CORES
echo EXECUTOR_MEMORY=$EXECUTOR_MEMORY
echo DRIVER_CORES=$DRIVER_CORES
echo DRIVER_MEMORY=$DRIVER_MEMORY

while(true)
    do /opt/spark/default/bin/spark-submit \
    --master yarn \
    --deploy-mode cluster \
    --num-executors $NUM_EXECUTORS \
    --executor-cores $EXECUTOR_CORES \
    --executor-memory $EXECUTOR_MEMORY \
    --driver-cores $DRIVER_CORES \
    --driver-memory $DRIVER_MEMORY \
    --class com.brunneis.App \
    hdfs://localhost:8020/aggregation-module.jar \
    --mariadb_host=localhost \
    --zookeeper_quorum=localhost \
    --start_timestamp=$(expr $(date +%s%3N) - 600000) \
    --exec_mode=1 &
    sleep 600
done
