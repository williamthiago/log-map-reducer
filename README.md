# Log Map Reducer

A partir de um conjunto de entradas de logs, separar os logs por usuário, ordenado por data, salvando cada conjunto de logs em seu respectivo arquivo, em uma saída informada.

## Cluster
- [Apache Spark](http://spark.apache.org/)

## Build

```bash
    sbt package
```

## Executando

```bash
    ./$SPARK_HOME/bin/spark-submit
	   --class "LogMapReducer" 
	   --master $SPARK_MASTER_URL 
	   target/scala-2.10/log-map-reducer_2.10-1.0.jar 
       -i "$SERVER1" 
       -i "$SERVER2"
       -i "$SERVER3" 
	   -o "$OUTPUT"
```

Onde:
  
* $SPARK_HOME é o caminho da instalação do Spark - Ex. _/home/user/spark/_
* $SPARK_MASTER_URL é a url do servidor do Spark - Ex. _spark://ip-do-servidor:7077_
* target/scala-2.10/log-map-reducer_2.10-1.0.jar é o resultado da compilação
* $SERVER1 e $OUTPUT são URI's suportadas pelo Hadoop (HDFS, sistema local, rede)
* Pode ser usado wildcards no input - Ex. _/path/to/folder/*.log_
* É possível informar mais de um input

#### Exemplo:

    ./home/user/spark/bin/spark-submit
         --class "LogMapReducer" 
         --master "spark://192.168.0.1:7077" 
         target/scala-2.10/log-map-reducer_2.10-1.0.jar
         -i "/tmp/server-1/logs/*.log"
         -i "/tmp/server-2/logs/*.log"
         -i "/tmp/server-3/logs/*.log"
         -i "/tmp/server-4/logs/*.log"
         -o "/tmp/server-1/outputs/"

## Exemplo de log

```txt
177.126.180.83 - - [15/Aug/2013:13:54:38 -0300] "GET /meme.jpg HTTP/1.1" 200 2148 "-" "userid=5352b590-05ac-11e3-9923-c3e7d8408f3a"
177.126.180.83 - - [15/Aug/2013:13:54:38 -0300] "GET /lolcats.jpg HTTP/1.1" 200 5143 "-" "userid=f85f124a-05cd-11e3-8a11-a8206608c529"
177.126.180.83 - - [15/Aug/2013:13:57:48 -0300] "GET /lolcats.jpg HTTP/1.1" 200 5143 "-" "userid=5352b590-05ac-11e3-9923-c3e7d8408f3a"
```