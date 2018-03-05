def deploy(Map settings) {
    def runOptions = ""
    String servicesYaml = settings.get('services-yaml', null)
    if (!servicesYaml) {
        error("'services-yaml' parameter not found. ")
    }
    runOptions = runOptions + " --services-yaml ${servicesYaml}"

    boolean taskDefinitionConfigEnv = settings.get('task-definition-config-env', true)
    if (!taskDefinitionConfigEnv) {
        runOptions = runOptions + " --no-task-definition-config-env"
    }

    String region = settings.get('region', 'us-east-1')
    runOptions = runOptions + " --region ${region}"

    String templateGroup = settings.get('template-group', null)
    if (!templateGroup) {
        error("'template-group' parameter not found. ")
    }
    runOptions = runOptions + " --template-group ${templateGroup}"

    String deployServiceGroup = settings.get('deploy-service-group', null)
    if (deployServiceGroup) {
        runOptions = runOptions + " --deploy-service-group ${deployServiceGroup}"
    }

    String threadsCount = settings.get('threads-count', null)
    if (threadsCount) {
        runOptions = runOptions + " --threads-count ${threadsCount}"
    }

    String serviceWaitMaxAttempts = settings.get('service-wait-max-attempts', null)
    if (serviceWaitMaxAttempts) {
        runOptions = runOptions + " --service-wait-max-attempts ${serviceWaitMaxAttempts}"
    }
    String serviceWaitDelay = settings.get('service-wait-delay', null)
    if (serviceWaitDelay) {
        runOptions = runOptions + " --service-wait-delay ${serviceWaitDelay}"
    }
    boolean serviceZeroKeep = settings.get('service-zero-keep', true)
    if (!serviceZeroKeep) {
        runOptions = runOptions + " --no-service-zero-keep"
    }
    boolean deleteUnusedService = settings.get('delete-unused-service', true)
    if (!deleteUnusedService) {
        runOptions = runOptions + " --no-delete-unused-service"
    }
    boolean stopBeforeDeploy = settings.get('stop-before-deploy', true)
    if (!stopBeforeDeploy) {
        runOptions = runOptions + " --no-stop-before-deploy"
    }
    script {
        docker.image('quay.io/wacul/aws-ecs').inside('--entrypoint ""') {
            sh "python3 /app/main.py service ${runOptions}"
        }
    }
}

def testTemplates(Map settings) {
    String runOptions = ""

    String servicesYaml = settings.get('services-yaml', null)
    if (!servicesYaml) {
        error("'services-yaml' parameter not found. ")
    }
    runOptions = runOptions + " --services-yaml ${servicesYaml}"

    boolean taskDefinitionConfigEnv = settings.get('task-definition-config-env', true)
    if (!taskDefinitionConfigEnv) {
        runOptions = runOptions + " --no-task-definition-config-env"
    }

    String environmentYamlDir = settings.get('environment-yaml-dir', null)
    if (!environmentYamlDir) {
        error("'environment-yaml-dir' parameter not found. ")
    }
    runOptions = runOptions + " --environment-yaml-dir ${environmentYamlDir}"

    script {
        docker.image('quay.io/wacul/aws-ecs').inside('--entrypoint ""') {
            sh "python3 /app/main.py test-templates ${runOptions}"
        }
    }
}
