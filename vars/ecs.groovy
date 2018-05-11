def deploy(Map settings) {
    def runOptions = ""
    String servicesYaml = settings.get('servicesYaml', null)
    if (!servicesYaml) {
        error("'servicesYaml' parameter not found. ")
    }
    runOptions = runOptions + " --services-yaml ${servicesYaml}"

    String environmentYaml = settings.get('environmentYaml', null)
    if (!environmentYaml) {
        error("'environment-yaml' parameter not found. ")
    }
    runOptions = runOptions + " --environment-yaml ${environmentYaml}"

    boolean taskDefinitionConfigEnv = settings.get('taskDefinitionConfigEnv', true)
    if (!taskDefinitionConfigEnv) {
        runOptions = runOptions + " --no-task-definition-config-env"
    }

    String region = settings.get('region', 'us-east-1')
    runOptions = runOptions + " --region ${region}"

    String templateGroup = settings.get('templateGroup', null)
    if (!templateGroup) {
        error("'template-group' parameter not found. ")
    }
    runOptions = runOptions + " --template-group ${templateGroup}"

    String deployServiceGroup = settings.get('deployServiceGroup', null)
    if (deployServiceGroup) {
        runOptions = runOptions + " --deploy-service-group ${deployServiceGroup}"
    }

    String threadsCount = settings.get('threadsCount', null)
    if (threadsCount) {
        runOptions = runOptions + " --threads-count ${threadsCount}"
    }

    String serviceWaitMaxAttempts = settings.get('serviceWaitMaxAttempts', null)
    if (serviceWaitMaxAttempts) {
        runOptions = runOptions + " --service-wait-max-attempts ${serviceWaitMaxAttempts}"
    }
    String serviceWaitDelay = settings.get('serviceWaitDelay', null)
    if (serviceWaitDelay) {
        runOptions = runOptions + " --service-wait-delay ${serviceWaitDelay}"
    }
    boolean serviceZeroKeep = settings.get('serviceZeroKeep', true)
    if (!serviceZeroKeep) {
        runOptions = runOptions + " --no-service-zero-keep"
    }
    boolean deleteUnusedService = settings.get('deleteUnusedService', true)
    if (!deleteUnusedService) {
        runOptions = runOptions + " --no-delete-unused-service"
    }
    boolean stopBeforeDeploy = settings.get('stopBeforeDeploy', true)
    if (!stopBeforeDeploy) {
        runOptions = runOptions + " --no-stop-before-deploy"
    }
    def awsEcs = docker.image('quay.io/wacul/aws-ecs')
    awsEcs.pull()
    awsEcs.inside('--entrypoint ""') {
        sh "python3 /app/main.py service ${runOptions}"
    }
}

def testTemplates(Map settings) {
    String runOptions = ""

    String servicesYaml = settings.get('servicesYaml', null)
    if (!servicesYaml) {
        error("'services-yaml' parameter not found. ")
    }
    runOptions = runOptions + " --services-yaml ${servicesYaml}"

    boolean taskDefinitionConfigEnv = settings.get('taskDefinitionConfigEnv', true)
    if (!taskDefinitionConfigEnv) {
        runOptions = runOptions + " --no-task-definition-config-env"
    }

    String environmentYamlDir = settings.get('environmentYamlDir', null)
    if (!environmentYamlDir) {
        error("'environment-yaml-dir' parameter not found. ")
    }
    runOptions = runOptions + " --environment-yaml-dir ${environmentYamlDir}"

    docker.image('quay.io/wacul/aws-ecs').inside('--entrypoint ""') {
        sh "python3 /app/main.py test-templates ${runOptions}"
    }
}
