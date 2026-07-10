pipeline {
    agent any

    environment {
        LOGIN_IMAGE = "login-service:latest"
        PRODUCT_IMAGE = "product-service:latest"
        GATEWAY_IMAGE = "api-gateway:latest"
    }

    stages {

        stage('Checkout Source') {
            steps {
                checkout scm
            }
        }

        stage('Build Login Service') {
            steps {
                dir('login-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Product Service') {
            steps {
                dir('product-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build API Gateway') {
            steps {
                dir('api-gateway') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                dir('login-service') {
                    sh 'docker build -t $LOGIN_IMAGE .'
                }

                dir('product-service') {
                    sh 'docker build -t $PRODUCT_IMAGE .'
                }

                dir('api-gateway') {
                    sh 'docker build -t $GATEWAY_IMAGE .'
                }
            }
        }

        stage('Verify Docker Images') {
            steps {
                sh 'docker images'
            }
        }
    }

    post {
        success {
            echo 'Build completed successfully.'
        }

        failure {
            echo 'Build failed.'
        }
    }
}
