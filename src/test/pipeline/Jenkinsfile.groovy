def getBuildDate() {
    def now = new Date()
    def formatter = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(now)
}

pipeline {
    agent any
    environment {
        harborRegistryUrl = 'core.harbor.domain'
        harborCredentialId = 'harbor_credentials'
        repositoryName = 'beijing/alpine-test'
    }
    stages {
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
                    waitForHarborWebHook server: 'Harbor Example', credentialsId: 'harbor_credentials', severity: 'Medium', abortPipeline: true
                    sh "docker rmi ${harborRegistryUrl}/${repositoryName}:${BUILD_NUMBER}"
                }
            }
        }
    }
}