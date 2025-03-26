export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
mvn install -Dmaven.test.skip=true --settings /Users/colin/.m2/default.xml
docker build -t spring-detail .
kubectl delete -f ../../config/deploy/default/spring-detail.yaml
minikube image load spring-detail -p multinode
kubectl apply -f ../../config/deploy/default/spring-detail.yaml