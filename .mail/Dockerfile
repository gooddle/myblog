# Dockerfile for mail service
FROM alpine:latest

# Install Postfix
RUN apk add --no-cache postfix

# Copy configuration files if needed
# COPY postfix/main.cf /etc/postfix/

# Expose the SMTP port
EXPOSE 587

# Start Postfix
CMD ["postfix", "start-fg"]