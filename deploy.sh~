mkdir siwimi
cd siwimi
git clone https://kuentingshiu@github.com/kuentingshiu/siwimi-rest.git
cd siwimi-rest
gradle wrapper
./gradlew build	
cd /usr/local/tomcat8/webapps
sudo /etc/init.d/tomcat8 stop
sudo rm siwimi*.war
sudo rm -rf siwimi*
sudo mv ~/siwimi/siwimi-rest/build/libs/siwimi-webapi-0.0.1.war .
sudo /etc/init.d/tomcat8 start
cd
sudo rm -rf siwimi

