# "Under maintenance" page

## When you see it

If the ERP application server stops responding — a restart, a deployment, or
scheduled maintenance — the client no longer shows broken screens or a login loop.
Instead it shows a full-page notice:

> **The ERP system is temporarily unavailable**
> The application server is not responding right now — it may be restarting or
> undergoing scheduled maintenance. Your work is not lost; please wait a moment.

## What it does

- It **checks automatically** every 10 seconds whether the server is back (a countdown
  shows the time to the next check).
- **Retry now** forces an immediate check.
- As soon as the server responds, you are taken **back to the page you were on**. If you
  were signed in, your session is still valid.

You do not need to reload the browser or sign in again.

## Notes

- A brief blip (for example navigating away while a request is still in flight) does
  **not** trigger this page — the client double-checks the server is really down before
  showing it.
- Health checks and the maintenance page itself keep working while the rest of the API
  is unavailable.
