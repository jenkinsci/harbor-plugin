def getBuildDate() {
    def now = new Date()
    def formatter = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(now)
}

pipeline {
    agent any
    environment {
        harborRegistryUrl = 'harbor.example.com'
        harborCredentialId = 'harbor_credentials'
        repositoryName = 'beijing/alpine-test'
    }
    stages {
        stage('CHECKOUT') {
            steps {
                checkout scm
            }
        }
        stage('DOCKER_BUILD') {
            steps {
                dir("src/test/pipeline") {
                    script {
                        docker.build("${harborRegistryUrl}/${repositoryName}:${BUILD_NUMBER}", "--build-arg BUILD_DATE=${getBuildDate()} .")
                    }
                }
            }
        }
        stage('DOCKER_PUSH') {
            steps {
                script {
                    docker.withRegistry("https://${harborRegistryUrl}", harborCredentialId) {
                        docker.image("${harborRegistryUrl}/${repositoryName}:${BUILD_NUMBER}").push()
                    }
                    sh "docker rmi ${harborRegistryUrl}/${repositoryName}:${BUILD_NUMBER}"
                }
            }
        }
    }
}