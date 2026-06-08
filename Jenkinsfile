pipeline {
    agent any
   
    stages {
        stage('Diagnose') {
            steps {
                sh 'cat /etc/resolv.conf'
                sh 'curl -I https://maven.aliyun.com 2>&1 | head -5'
                sh 'mvn -version'
                sh 'echo $HOME'
                sh 'cat $HOME/.m2/settings.xml'
            }
        }
        stage('Build1') {
            steps {
                sh 'mvn clean compile -DskipTests'
            }
        }
        stage('Test1') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/surefire-reports/*.xml'
                }
            }
        }
        
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
