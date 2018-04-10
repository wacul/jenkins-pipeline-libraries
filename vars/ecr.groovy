def addTag(Map settings) {
    String region = settings.get('region', 'us-east-1')
    String awsRegistryId = settings.get('awsRegistryId', null)
    if (!awsRegistryId) {
        error("'awsRegistryId' parameter not found. ")
    }
    String sourceTag = settings.get('sourceTag', null)
    if (!sourceTag) {
        error("'sourceTag' parameter not found. ")
    }
    String addTag = settings.get('addTag', null)
    if (!addTag) {
        error("'addTag' parameter not found. ")
    }
    String repository = settings.get('repository', null)
    if (!repository) {
        error("'repository' parameter not found. ")
    }
    def imageManifest = sh returnStdout: true,
        script: """
            aws ecr batch-get-image \
                --region \'${region}\' \
                --registry-id \"${awsRegistryId}\" \
                --repository-name \"${repository}\" \
                --image-ids imageTag=\"${sourceTag}\" \
                --query images[].imageManifest \
                --output text|head -c -1
        """
    if (imageManifest?.trim()) {
        error("ECR repository Image tag not found.")
    }
    sh script: """
        aws ecr put-image \
            --region "${region}" \
            --registry-id "${awsRegistryId}" \
            --repository-name "${repository}" \
            --image-tag "${addTag}" \
            --image-manifest \'${imageManifest}\'
    """
}
