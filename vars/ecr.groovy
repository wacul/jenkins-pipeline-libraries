def addTag(Map settings) {
    def imageManifest = ['aws', 'ecr', 'batch-get-image', '--registry-id', "${settings.awsRegistryId}", '--repository-name', "${settings.repository}", "--image-ids", "imageTag=\"${settings.sourceTag}\"", '--query', 'images[].imageManifest', '--output', 'text'].execute()
    imageManifest.waitFor()
    if (imageManifest.exitValue()) {
        println imageManifest.err.text
        throw new Exception("Cannot retrieve image manifest from ECR.")
    }
    def manifestText = imageManifest.text
    def manifestJson = manifestText.substring(0, manifestText.length() - 1)
    def addTag = ['aws', 'ecr', 'put-image', '--registry-id', "${settings.awsRegistryId}", '--repository-name', "${settings.repository}", '--image-tag', "${settings.addTag}", '--image-manifest', "${manifestJson}"].execute()
    addTag.waitFor()
    if (addTag.exitValue()) {
        println addTag.err.text
        throw new Exception("Cannot put image manifest to ECR.")
    }
}
