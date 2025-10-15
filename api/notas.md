API
* Enviar mensagem de deposito e transferência 
  * adicionar correlationId na transação
* Receber o retorno via SQS (SNS?)
* Provisionar DynamoDB para armazenar os dados de transação
* Gerar arquivo do extrado das transações e armazenar em um S3
* add AWS Cognito para autenticação
* usar o elastiCache 

wallet-transaction-processor

* Enviar notificação do deposito e da transferência via SNS
* Cria DLQ para as filas
  * provisionar DLQs via terraform

        redrive_policy = jsonencode({
            deadLetterTargetArn = aws_sqs_queue.deposit_dlq.arn
            maxReceiveCount     = 5
        })
