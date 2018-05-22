FROM docker.int.thomsonreuters.com:5001/risk-tech/base/tomcat85
ADD target/mock-namematcher.war webapps/
