pipeline {
    agent any
    tools {
        maven 'Maven_3'
        jdk 'jdk11'
    }
    stages {
        stage('Initialize') {
            steps {
                bat '''
                     echo "PATH = ${PATH}"
                     echo "M2_HOME = ${M2_HOME}"
                 '''
            }
        }

        stage('Build') {
           steps {
              bat 'mvn -Dmaven.test.failure.ignore=true install'
           }
           post {
               success {
               junit 'target/surefire-reports/**/*.xml'
               }
           }
         }
         //commenting as docker is not working locally due to access privilege issue
        /** stage('Build Docker Image') {
             steps {
                bat 'docker build -t promotion-engine .'
             }
          }
         stage('Docker Run Image') {
            steps {
               bat 'docker run -dp 4200:8080 promotion-engine'
            }
          } **/
    }
}