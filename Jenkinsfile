pipeline {
    agent any

    environment {
        IMAGE_NAME = "snowflakeID-app"
        IMAGE_TAG  = "${env.BUILD_NUMBER}"
        CONTAINER  = "snowflakeID-app"
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
                    # 停止并删除旧容器
                    docker stop ${CONTAINER} || true
                    docker rm   ${CONTAINER} || true

                    # 启动新容器
                    docker run -d \
                        --name ${CONTAINER} \
                        --restart unless-stopped \
                        -p ${APP_PORT}:8080 \
                        ${IMAGE_NAME}:${IMAGE_TAG}
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
        always {
            // 清理旧镜像，只保留最近3个版本
            sh """
                docker images ${IMAGE_NAME} --format '{{.Tag}}' \
                    | sort -n \
                    | head -n -3 \
                    | xargs -r -I{} docker rmi ${IMAGE_NAME}:{} || true
            """
        }
    }
}
