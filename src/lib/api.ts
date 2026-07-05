// ─── API ───────────────────────────────────────────────────────────────────────
export const API_BASE =
  "https://script.google.com/macros/s/AKfycbwoDnE3JlArcGOew4IBVlQA_CPo9i2Rg1l2_L6sSlewtEWi3StqruW99WGPGZivj7Fy/exec";

// ─── Types ─────────────────────────────────────────────────────────────────────
export interface Supplier  { ID: number; Name: string; Phone?: string; Address?: string; }
export interface Customer  { ID: number; Name: string; Phone?: string; Address?: string; }

/**
 * Product: defined once in the Products sheet.
 * Quantity     = opening/initial stock when the product was first entered.
 * Purchase_Price = optional cost per unit at the time of definition.
 *                  (New purchases use Supplier_Transactions.Unit_Price)
 * No selling price — selling price is set per invoice.
 *
 * Google Sheets columns required:
 *   ID | Name | Quantity | Purchase_Price
 */
export interface Product {
  ID: number;
  Name: string;
  Quantity: number;        // initial/opening stock (can be 0 or empty)
  Purchase_Price: number;  // optional opening cost per unit
}

export interface InventoryAdjustment {
  ID: number;
  Product_ID: number;
  Product_Name: string;
  Quantity: number;
  Unit_Cost: number;
  Date: string;
  Note: string;
}

export interface SupplierTransaction {
  ID: number;
  Invoice_ID: string;
  Supplier_ID: number;
  Supplier_Name: string;
  Product_ID: number;
  Product_Name: string;
  Quantity: number;
  Unit_Price: number;
  Total_Price: number;
  Date: string;
  Note: string;  // optional expiry / other note — stored in the 'Note' column
}

export interface CustomerTransaction {
  ID: number;
  Invoice_ID: string;
  Customer_ID: number;
  Customer_Name: string;
  Product_ID: number;
  Product_Name: string;
  Quantity: number;
  Unit_Price: number;
  Total_Price: number;
  Date: string;
}

export interface SupplierPayment { ID: number; Supplier_ID: number; Supplier_Name: string; Amount: number; Date: string; }
export interface CustomerPayment { ID: number; Customer_ID: number; Customer_Name: string; Amount: number; Date: string; }

// ─── Fetch ─────────────────────────────────────────────────────────────────────
export async function fetchSheet<T>(sheetName: string): Promise<T[]> {
  const res = await fetch(`${API_BASE}?sheetName=${encodeURIComponent(sheetName)}`, { cache: "no-store" });
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  const json = await res.json();
  return Array.isArray(json) ? (json as T[]) : [];
}

// ─── Write ─────────────────────────────────────────────────────────────────────
async function send(sheetName: string, body: Record<string, unknown>): Promise<void> {
  await fetch(`${API_BASE}?sheetName=${encodeURIComponent(sheetName)}`, {
    method: "POST",
    mode: "no-cors",
    body: JSON.stringify(body),
  });
}

export async function postSheet(sheetName: string, data: Record<string, unknown>): Promise<void> {
  await send(sheetName, { action: "create", ...data });
}

/**
 * Multi-row insert for multi-product invoices.
 * Apps Script: else if (action==="create_multi") { for(row of payload.rows) sheet.appendRow(...) }
 */
export async function postMultiSheet(sheetName: string, rows: Record<string, unknown>[]): Promise<void> {
  await send(sheetName, { action: "create_multi", rows });
}

export async function updateSheet(sheetName: string, data: Record<string, unknown>): Promise<void> {
  await send(sheetName, { action: "update", ...data });
}

export async function deleteSheet(sheetName: string, id: number | string): Promise<void> {
  await send(sheetName, { action: "delete", ID: id });
}

export async function renumberSheet(sheetName: string, foreignKey: string, relatedSheets: string[]): Promise<void> {
  await send(sheetName, { action: "renumber", foreignKey, relatedSheets });
}

// ─── Stock helpers ─────────────────────────────────────────────────────────────
/**
 * Current stock for a product:
 *   Product.Quantity (initial/opening)
 * + Inventory_Adjustments.Quantity (optional extra adjustments)
 * + Supplier_Transactions.Quantity  (purchases)
 * - Customer_Transactions.Quantity  (sales)
 */
export function calcProductStock(
  productId: string | number,
  products: Product[],
  adjustments: InventoryAdjustment[],
  supTxns: SupplierTransaction[],
  cusTxns: CustomerTransaction[],
): number {
  const pid     = String(productId);
  const product = products.find(p => String(p.ID) === pid);
  const initial = Number(product?.Quantity || 0);
  const fromAdj = adjustments.filter(a => String(a.Product_ID) === pid).reduce((s, a) => s + Number(a.Quantity || 0), 0);
  const bought  = supTxns.filter(t => String(t.Product_ID) === pid).reduce((s, t) => s + Number(t.Quantity || 0), 0);
  const sold    = cusTxns.filter(t => String(t.Product_ID) === pid).reduce((s, t) => s + Number(t.Quantity || 0), 0);
  return initial + fromAdj + bought - sold;
}

/**
 * Weighted average purchase cost:
 *   base = Product.Purchase_Price × Product.Quantity  (if both > 0)
 *   + Inventory_Adjustments with Unit_Cost > 0
 *   + Supplier_Transactions Unit_Price
 */
export function calcAvgCost(
  productId: string | number,
  products: Product[],
  adjustments: InventoryAdjustment[],
  supTxns: SupplierTransaction[],
): number {
  const pid     = String(productId);
  const product = products.find(p => String(p.ID) === pid);
  const initQty  = Number(product?.Quantity || 0);
  const initCost = Number(product?.Purchase_Price || 0);

  const rows: { qty: number; cost: number }[] = [];
  if (initQty > 0 && initCost > 0) rows.push({ qty: initQty, cost: initCost });

  adjustments
    .filter(a => String(a.Product_ID) === pid && Number(a.Unit_Cost) > 0)
    .forEach(a => rows.push({ qty: Number(a.Quantity), cost: Number(a.Unit_Cost) }));

  supTxns
    .filter(t => String(t.Product_ID) === pid && Number(t.Unit_Price) > 0)
    .forEach(t => rows.push({ qty: Number(t.Quantity), cost: Number(t.Unit_Price) }));

  const totalQty  = rows.reduce((s, r) => s + r.qty, 0);
  const totalCost = rows.reduce((s, r) => s + r.qty * r.cost, 0);
  return totalQty > 0 ? totalCost / totalQty : 0;
}

// ─── Phone validation ──────────────────────────────────────────────────────────
const EG_PHONE = /^01[0125]\d{8}$/;
export function validateEgPhone(phone: unknown): string | null {
  const s = phone == null ? "" : String(phone);
  if (!s.trim()) return null;
  if (!EG_PHONE.test(s.replace(/[\s\-()]/g, "")))
    return "رقم غير صحيح — الصيغة: 01XXXXXXXXX (11 رقم، يبدأ بـ 010 / 011 / 012 / 015)";
  return null;
}

// ─── Utils ─────────────────────────────────────────────────────────────────────
export function maxId(arr: { ID: number | string }[]): number {
  if (!arr.length) return 1;
  return Math.max(...arr.map(r => Number(r.ID ?? 0))) + 1;
}
export function todayStr(): string { return new Date().toISOString().split("T")[0]; }
export function fmtCurrency(n: number | string | undefined): string {
  return Number(n ?? 0).toLocaleString("ar-EG", { minimumFractionDigits: 0, maximumFractionDigits: 2 });
}
export function fmtDate(d: string | undefined): string {
  if (!d) return "—";
  try { return new Date(d).toLocaleDateString("ar-EG", { year: "numeric", month: "short", day: "numeric" }); }
  catch { return d; }
}
