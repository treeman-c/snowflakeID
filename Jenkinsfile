pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'echo "开始构建..."'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                sh 'echo "打包完成"'
            }
        }
    }

    post {
        success { echo '✅ 构建成功' }
        failure { echo '❌ 构建失败' }
    }
}
