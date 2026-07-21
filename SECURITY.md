# Security Policy

## Supported Versions

The following versions of Rakshak DTU are currently receiving security updates:

| Version | Supported          |
| ------- | ------------------ |
| 1.2.x   | :white_check_mark: |
| 1.1.x   | :x:                |
| 1.0.x   | :x:                |
| < 1.0   | :x:                |

Only the latest released version is actively maintained. If you're running an older version, please upgrade before reporting a vulnerability so we can confirm it's still present.

## Reporting a Vulnerability

If you discover a security vulnerability in Rakshak DTU (e.g. issues affecting the vehicle database, authentication, camera/log data, or number-plate detection pipeline), please **do not open a public GitHub issue**. Instead:

- **Email:** [ekanshbhushan2k22@gmail.com] with the subject line `[SECURITY] Rakshak DTU`
- **Alternative:** Open a [GitHub Security Advisory](https://github.com/Ekansh-Bhushan/Rakshak-DTU/security/advisories/new) (private, visible only to maintainers)

Please include:
- A description of the vulnerability and its potential impact
- Steps to reproduce (proof-of-concept code or screenshots if applicable)
- The affected version/commit

### What to expect

- **Acknowledgement:** within 3–5 days of your report
- **Status updates:** at least every 7–10 days while the issue is being investigated
- **If accepted:** a fix will be prioritized, and you'll be credited in the release notes (unless you prefer to stay anonymous)
- **If declined:** you'll receive an explanation of why the report doesn't qualify as a vulnerability or isn't currently exploitable

### Scope

This policy covers the Rakshak DTU Android application and its associated backend/database logic in this repository. It does not cover third-party services or infrastructure (e.g. campus CCTV hardware, cloud hosting) unless the vulnerability originates from code in this repo.

Thank you for helping keep Rakshak DTU and DTU campus security data safe.
