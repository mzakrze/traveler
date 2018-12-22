build:
	cd frontend; make build
	cd frontend; cp -R build/* ../src/main/resources/public
	cd src/main/resources/secrets; gpg2 -d google_api_key.gpg
	./gradlew build
	-rm src/main/resources/secrets/google_api_keys

clean:
	./gradlew clean

local_deploy: clean build
	java -jar ./build/libs/Traveler-0.0.1-SNAPSHOT.war
	google-chrome localhost/api/stub/simple_json


heroku_deploy: clean build
	heroku war:deploy build/libs/Traveler-0.0.1-SNAPSHOT.war --app spdb-traveler-app
	google-chrome https://spdb-traveler-app.herokuapp.com
