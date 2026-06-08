pipeline {
    agent any

    environment {
        MAVEN_OPTS = '-Dhttp.proxyHost=172.20.208.1 -Dhttp.proxyPort=10808 -Dhttps.proxyHost=172.20.208.1 -Dhttps.proxyPort=10808'
    }
    
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
