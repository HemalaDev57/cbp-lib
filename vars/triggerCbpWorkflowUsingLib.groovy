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

        def response = httpRequest(
            httpMode: 'POST',
            url: "${cbpUrl}/v3/components/${componentId}/trigger",
            contentType: 'APPLICATION_JSON',
            requestBody: jsonPayload,
            customHeaders: [[name: 'Authorization', value: "Bearer ${TOKEN}"]]
        )
        echo "CBP Triggered. Response: ${response.status}"
        echo "Response body:\n${response.getContent()}"
    }
}
