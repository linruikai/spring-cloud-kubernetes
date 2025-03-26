export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
mvn clean install -Dmaven.test.skip=true --settings /Users/colin/.m2/default.xml
docker build -t spring-user-gray .
kubectl delete -f ../../config/deploy/gray/spring-user.yaml
minikube image load spring-user-gray -p multinode
kubectl apply -f ../../config/deploy/gray/spring-user.yaml