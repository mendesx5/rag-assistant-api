# rag-assistant-api

Java / Spring Boot backend for the **Corporate RAG Assistant** - a portfolio project where users upload PDF documents and then ask natural-language questions about them, receiving AI-generated answers based strictly on the uploaded content (Retrieval-Augmented Generation), instead of the model "making things up".

This service is the **Front Desk**: it's the entry point the user actually talks to. It handles authentication, receives document uploads, publishes processing jobs to a queue, exposes the chat endpoint, and persists all metadata and conversation history.

The heavy AI work (reading PDFs, generating embeddings, talking to the LLM) happens in a separate service - [`rag-assistant-ai-service`](https://github.com/mendesx5/rag-assistant-ai-service) - so it can scale and evolve independently from this one.

## Architecture at a glance

```
User → [rag-assistant-api (Java)] → RabbitMQ → [rag-assistant-ai-service (Python)] → ChromaDB / Groq LLM
                 ↓
            PostgreSQL
```

Shared local infrastructure (PostgreSQL + RabbitMQ) lives in [`rag-assistant-infra`](https://github.com/mendesx5/rag-assistant-infra) - start that first.

## Tech stack

- Java 17
- Spring Boot 4.1.1
- Maven
- Spring Data JPA + Flyway
- Spring AMQP (RabbitMQ)
- PostgreSQL
- `application.yaml` configuration

## Getting started

```bash
# 1. Start the shared infrastructure first (see rag-assistant-infra)
# 2. Then:
git clone https://github.com/mendesx5/rag-assistant-api.git
cd rag-assistant-api
./mvnw spring-boot:run
```

Copy `.env.example` to `.env` and fill in your local values before running (never commit `.env`).

## Project roadmap

The full project is planned in 6 weekly sprints (Scrum). This repository is the Java side of Sprints 0, 1, 2, 5, and 6.

| Sprint | Focus | Touches this repo? |
|---|---|---|
| 0 | Environment foundation | Partly - repo setup |
| **1** | **Document modeling & upload endpoint** | ✅ Yes |
| **2** | **Async messaging (publish to queue)** | ✅ Yes |
| 3 | Python - queue consumption | No |
| 4 | Python - RAG core | No |
| **5** | **Chat endpoint & history** | ✅ Yes |
| **6** | **UI, resilience, documentation** | ✅ Yes |

## Current status: Sprint 0

**Goal:** have an identical development environment running on both development machines, ready to receive code.

- [x] Repository created (Spring Boot 4.1.1, Java 17, Maven) and set to private
- [ ] Connected to the shared `rag-assistant-infra` environment
- [ ] `.env.example` documented

## Coming up next — Sprint 1

**Goal:** the user can upload a PDF through the API and the system records it reliably.

- [x] `Document` entity (id, name, status, upload date, owner)
- [x] Flyway initial migration
- [x] `POST /api/documents` endpoint receiving a `MultipartFile`
- [x] File saved to local disk (or temp folder) + metadata saved to PostgreSQL
- [x] File type/size validation

## Why this architecture

Java and Python live in separate repositories on purpose — it mirrors how the two services are meant to scale, deploy, and evolve independently in a real microservices setup, rather than being coupled together in a single deployable unit.
