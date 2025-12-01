# Lease payment upload how-to

1. Open **Maintenance > Lease Payment Upload** in the Lease module.
2. Pick the IFRS16 lease contract you are loading payments for.
3. Click **Choose file** and select a CSV with `paymentDate` and `paymentAmount` columns (optional `active` column to override defaults).
4. Leave **Launch batch immediately** checked to process the file as soon as it is stored, then click **Submit Upload**.
5. After a successful request, note the upload id and stored file name in the success banner.
6. Review the **Previous uploads** table for status, active flag, and file name. Use **Deactivate** to retire an incorrect upload; the related lease payments are marked inactive at the same time.
7. Re-upload a corrected CSV when needed. Amortisation compilation only consumes active payments tied to active uploads.
