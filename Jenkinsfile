pipeline {
    agent any

    environment {
        IMAGE_NAME = "snowflake-app"
        IMAGE_TAG  = "${env.BUILD_NUMBER}"
        CONTAINER  = "snowflake-app"
        APP_PORT   = "9000"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-17'
                    args  '-v /root/.m2:/root/.m2'
                    reuseNode true
                }
            }
            steps {
                sh 'mvn clean package -DskipTests'
            }
            post {
                always {
                    junit allowEmptyResults: true,
                          testResults: '**/surefire-reports/*.xml'
                }
            }
        }

        stage('Build Image') {
            steps {
                sh """
                    docker build \
                        -t ${IMAGE_NAME}:${IMAGE_TAG} \
                        -t ${IMAGE_NAME}:latest \
                        .
                """
            }
        }


        
        stage('Deploy') {
            steps {
                sh """
                    chmod +x deploy.sh
                    ./deploy.sh \
                        ${IMAGE_NAME}:${IMAGE_TAG} \
                        ${CONTAINER} \
                        ${APP_PORT}
                """
            }
        }
        
        stage('run docker-compose container') {
            steps {
                sh """
                    docker compose up -d
                    
                """
            }
        }

    }

    post {
        success {
            echo "✅ 部署成功，访问 http://localhost:${APP_PORT}"
        }
        failure {
            echo "❌ 部署失败，查看上方日志"
        }
    }
}
