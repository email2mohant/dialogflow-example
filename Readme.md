docker run -d -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama
docker exec -it ollama ollama run llama2
docker run -d -p 12000:8000 ghcr.io/chroma-core/chroma:latest
docker run -d -p 12000:9200 -p 12600:9600 -e "discovery.type=single-node" -e "DISABLE_INSTALL_DEMO_CONFIG=true" -e "DISABLE_SECURITY_PLUGIN=true" opensearchproject/opensearch:latest

https://www.kotak.com/content/dam/Kotak/investor-relation/Financial-Result/Annual-Reports/FY-2023/kotak-mahindra-bank/kotak-mahindra-bank-limited-FY22-23.pdf
