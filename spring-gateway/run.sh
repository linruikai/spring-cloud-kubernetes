export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
mvn clean install -Dmaven.test.skip=true --settings /Users/colin/.m2/default.xml
docker build -t spring-gateway .
kubectl delete -f ../config/deploy/spring-gateway.yaml
minikube image load spring-gateway -p multinode
kubectl apply -f ../config/deploy/spring-gateway.yaml