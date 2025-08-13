# Project Rules

## Code Style
- Use Java 8+ compatible code.
- Follow Google Checkstyle (applied during `mvn validate`). Fix warnings when touching related code.
- Naming: use descriptive names; avoid abbreviations.
- Keep methods short and focused; prefer early returns.

## Formatting
- Line endings: CRLF on Windows, LF accepted in Markdown/YAML.
- Indentation: spaces (4 for Java, 2 for XML/MD/properties).
- No trailing whitespace; ensure final newline.

## Architecture
- Controllers call services; services call repositories.
- Do not access repositories directly from controllers.
- Validate input at the boundary (controller/service) as needed.

## Security
- Basic Auth is enabled; do not remove security for `/api/**`.
- Keep H2 console enabled for local dev only.

## Logging
- Use SLF4J (`LoggerFactory.getLogger(...)`).
- Do not log secrets.
- Rely on AOP logging for timing; avoid excessive debug logs.

## Git & CI
- Commit small, atomic changes with clear messages.
- Do not commit build artifacts; `target/` is ignored.
- Default branch: `main`.

## Build
- Use Maven. Commands:
  - `mvn validate` (Checkstyle)
  - `mvn test`
  - `mvn package`

## Reviews
- Open PRs with a brief description and testing notes.
- Link related issues.


