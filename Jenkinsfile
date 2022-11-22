def gv

pipeline{
    agent any
    tools {
        maven 'maven-3.8'
    }
    stages{
        stage("init"){
            steps{
                script{
                    gv = load "script.groovy"
                }
            }
        }
        stage("incrementVersion"){
            steps{
                script{
                        echo "incrementing app version......"
                        sh 'mvn build-helper:parse-version versions:set \
                            -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                            versions:commit'
                        def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
                        def version = matcher[1][1]
                        env.IMAGE_VERSION = "$version-$BUILD_NUMBER"
                }
            }
        }
        stage("build jar"){
            steps{
                script{
                        echo "building the application jar....."
                        sh 'mvn clean package -Dmaven.test.skip=true'
                }    
            }
        }
        stage("build image"){
            steps{
                script{
                    echo "building the application image....."
                    withCredentials([
                        usernamePassword(credentialsId: 'dockerhub-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')
                    ]){
                        sh "docker build -t devanshudockerhub/springboot-app:$IMAGE_VERSION ."
                        sh "echo $PASS | docker login -u $USER --password-stdin"
                        sh "docker push devanshudockerhub/springboot-app:$IMAGE_VERSION"
                    }
                }    
            }
        }
        stage("commit version update"){
            steps{
                script{
                    withCredentials([
                        usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')
                    ]){
                        sh 'git config --global user.email "jenkins@jenkins.com"'
                        sh 'git config --global user.name "jenkins"'

                        sh 'git status'
                        sh 'git branch'
                        sh 'git config --list'

                        sh "git remote set-url origin https://${USER}:${PASS}@github.com/devanshu1github/my_project.git"
                        sh 'git add .'
                        sh 'git commit -m "jenkins: version bump"'
                        sh 'git push origin HEAD:main'
                    }
                }
            }
        }
    }
}