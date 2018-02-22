def addTag(String awsRegistryId, String repository, String sourceTag, String addTag) {
    def imageManifest = sh(script: aws ecr batch-get-image --registry-id "${awsRegistryId}" --repository-name "${repository}" --image-ids "imageTag=${sourceTag}" --query images[].imageManifest --output text|head -c -1).text
    sh(script: aws ecr put-image --registry-id "${awsRegistryId}" --repository-name "${repository}" --image-tag "${addTag}" --image-manifest "${imageManifest}", returnStdout: true)
}
