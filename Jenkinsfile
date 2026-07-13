pipeline {
    agent any

    environment {
        DOCKER_HUB = "jyotidp"

        LOGIN_IMAGE   = "${DOCKER_HUB}/login-service:latest"
        PRODUCT_IMAGE = "${DOCKER_HUB}/product-service:latest"
        GATEWAY_IMAGE = "${DOCKER_HUB}/api-gateway:latest"
    }

    stages {

        stage('Checkout Source') {
            steps {
                checkout scm
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir('login-service') {
                    withSonarQubeEnv('SonarQube') {
                        sh '''
                            mvn clean verify sonar:sonar \
                            -Dsonar.projectKey=login-service \
                            -Dsonar.projectName=login-service
                        '''
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build Login Service') {
            steps {
                dir('login-service') {
                    sh 'mvn clean deploy -DskipTests'
                }
            }
        }

        stage('Build Product Service') {
            steps {
                dir('product-service') {
                    sh 'mvn clean deploy -DskipTests'
                }
            }
        }

        stage('Build API Gateway') {
            steps {
                dir('api-gateway') {
                    sh 'mvn clean deploy -DskipTests'
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

        stage('Docker Login') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {

                    sh '''
                        echo "$DOCKER_PASS" | docker login \
                        -u "$DOCKER_USER" \
                        --password-stdin
                    '''
                }
            }
        }

        stage('Push Docker Images') {
            steps {

                sh 'docker push $LOGIN_IMAGE'
                sh 'docker push $PRODUCT_IMAGE'
                sh 'docker push $GATEWAY_IMAGE'

            }
        }

        stage('Verify Images') {
            steps {
                sh 'docker images'
            }
        }

    }

    post {

        always {
            sh 'docker logout || true'
            cleanWs()
        }

        success {
            echo 'CI/CD Pipeline Completed Successfully.'
        }

        failure {
            echo 'Pipeline Failed.'
        }

    }
}
