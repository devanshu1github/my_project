def incrementVersion(){
    echo "incrementing app version......"
    sh 'mvn build-helper:parse-version versions:set \
        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
        versions:commit'
    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    def version = matcher[1][1]
    env.IMAGE_VERSION = "$version-$BUILD_NUMBER"
}

def buildJar(){
    echo "building the application jar....."
    sh 'mvn clean package -Dmaven.test.skip=true'
}

def buildImage(String imageName){
    echo "building the application image....."
    withCredentials([
        usernamePassword(credentialsId: 'dockerhub-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')
        ]){
        sh "docker build -t $imageName ."
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh "docker push $imageName"
    }
}

return this