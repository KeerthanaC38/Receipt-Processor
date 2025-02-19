# Receipt Processor Project

This project is a receipt processing application. Below are the instructions on how to set up and run the project after cloning the repository from GitHub.

## Cloning the Repository

1. Clone the repository to your local machine using the following command:

   ```bash
   git clone https://github.com/KeerthanaC38/Receipt-Processor.git
    ```
   
2. Navigate into the project directory:

    ```bash
   cd Receipt-Processor
   ```
   
## Building and Running the Docker Container

To run the application using Docker, follow these steps:

1. Go to the folder containing the `Dockerfile`. Same folder

2. Build the Docker image with the following command:

    ```bash
   docker build -t receipt-processor .
   ```
   
3. Once the build is complete, run the Docker container with:

    ```bash
   docker run -p 8080:8080 receipt-processor
   ```
   This will start the application and expose it on port `8080`.

## Testing the application

1. Through Postman if it is installed on your local.
2. Run the automated testing suite after the application is up and running.