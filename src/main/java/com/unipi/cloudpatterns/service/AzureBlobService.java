package com.unipi.cloudpatterns.service;

import com.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AzureBlobService {

    private final BlobServiceClient blobServiceClient;
    private final BlobContainerClient containerClient;

    public AzureBlobService(
            @Value("${azure.connection.string}") String azureConnectionString,
            @Value("${azure.storage.container-name}") String containerName) {

        this.blobServiceClient = new BlobServiceClientBuilder().connectionString(azureConnectionString).buildClient();
        this.containerClient = blobServiceClient.getBlobContainerClient(containerName);
    }

    public String getBlobUrl(String blobName) {
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        if (blobClient.exists()) {
            return blobClient.getBlobUrl();
        } else {
            throw new RuntimeException("Blob " + blobName + " does not exist in container " + containerClient.getBlobContainerName());
        }
    }
}

