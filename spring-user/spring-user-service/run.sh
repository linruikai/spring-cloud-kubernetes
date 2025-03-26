export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
mvn clean install -Dmaven.test.skip=true --settings /Users/colin/.m2/default.xml
docker build -t spring-user .
kubectl delete -f ../../config/deploy/default/spring-user.yaml
minikube image load spring-user -p multinode
kubectl apply -f ../../config/deploy/default/spring-user.yaml