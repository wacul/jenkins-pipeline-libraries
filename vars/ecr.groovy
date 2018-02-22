def addTag(Map settings) {
    def imageManifest = sh(returnStdout: true,, script: """aws ecr batch-get-image --registry-id "${settings.awsRegistryId}" --repository-name "${settings.repository}" --image-ids imageTag="${settings.sourceTag}" --query images[].imageManifest --output text|head -c 1""")
    sh(returnStdout: true, script: """aws ecr put-image --registry-id "${settings.awsRegistryId}" --repository-name "${settings.repository}" --image-tag "${settings.addTag}" --image-manifest "${imageManifest}" """)
}
