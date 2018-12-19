build:
	cd frontend; make build
	cd frontend; cp -R build/* ../src/main/resources/public
	./gradlew build

clean:
	./gradlew clean

local_deploy: clean build
	java -jar ./build/libs/Traveler-0.0.1-SNAPSHOT.war
	google-chrome localhost/api/stub/simple_json


heroku_deploy: clean build
	heroku war:deploy build/libs/Traveler-0.0.1-SNAPSHOT.war --app spdb-traveler-app
	google-chrome https://spdb-traveler-app.herokuapp.com/api/stub/simple_json
