def call(Map args) {
    def componentId = args.componentId
    def workflowFile = args.workflowFile
    def branch = args.branch
    def workflowInputs = args.get('workflowInputs', '{}')
    def cbpUrl = args.get('cbpUrl', 'https://api.saas-preprod.beescloud.com')

    withCredentials([string(credentialsId: 'cbp-preprod-token', variable: 'TOKEN')]) {
        def payload = [
            componentId      : componentId,
            workflowFileName : workflowFile,
            branchName       : branch,
            workflowInputs   : workflowInputs
        ]
        def jsonPayload = new groovy.json.JsonBuilder(payload).toString()

        // Logging the payload for debugging
        echo "Payload: ${jsonPayload}"

        // Using curl command to trigger the CBP workflow
        def curlCommand = """curl -X POST "${cbpUrl}/v3/components/${componentId}/trigger" \\
            -H "Authorization: Bearer ${TOKEN}" \\
            -H "Content-Type: application/json" \\
            -d '${jsonPayload}'"""
        
        echo "Sending request to CBP with command: ${curlCommand}"
        
        // Execute the curl command
        def response = sh(script: curlCommand, returnStdout: true).trim()

        // Logging the response from the CBP trigger
        echo "CBP Triggered. Response: ${response}"
    }
}
