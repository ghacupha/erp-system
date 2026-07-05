"use client";
import React, { useState, useMemo } from "react";
import { CreditCard, FileText, Truck, Users, TrendingDown, TrendingUp, DollarSign, Phone, MessageSquare } from "lucide-react";
import { useSheet } from "@/hooks/useSheet";
import {
  Supplier, Customer,
  SupplierTransaction, CustomerTransaction,
  SupplierPayment, CustomerPayment,
  postSheet, maxId, todayStr, fmtCurrency, fmtDate,
} from "@/lib/api";
import {
  LoadingState, ErrorState, EmptyState,
  Modal, FormGroup, SectionHeader, TabBar, Badge, StatCard,
} from "@/components/ui";

const normalizePhone = (phone?: string | number | null) => {
  const raw = phone == null ? "" : String(phone);
  return raw.replace(/\D/g, "");
};
const getPhoneHref = (phone?: string | number | null) => {
  const digits = normalizePhone(phone);
  return digits ? `tel:${digits}` : undefined;
};
const getWhatsappHref = (phone?: string | number | null) => {
  const digits = normalizePhone(phone);
  if (!digits) return undefined;
  return `https://wa.me/${digits.startsWith("0") ? `2${digits.slice(1)}` : digits}`;
};

// ─── Account Statement Modal ───────────────────────────────────────────────────
interface StatementEntry {
  date: string;
  type: "txn" | "payment";
  label: string;
  amount: number;
}

function AccountStatementModal({
  party, txns, payments, isSupplier, onClose,
}: {
  party: Supplier | Customer;
  txns: (SupplierTransaction | CustomerTransaction)[];
  payments: (SupplierPayment | CustomerPayment)[];
  isSupplier: boolean;
  onClose: () => void;
}) {
  const partyIdKey = isSupplier ? "Supplier_ID" : "Customer_ID";
  const pid = String(party.ID);

  const myTxns = txns.filter(t => String((t as any)[partyIdKey]) === pid);
  const myPmts = payments.filter(p => String((p as any)[partyIdKey]) === pid);

  const totalDebt = myTxns.reduce((s, t) => s + Number(t.Total_Price ?? 0), 0);
  const totalPaid = myPmts.reduce((s, p) => s + Number((p as any).Amount ?? 0), 0);
  const remaining = totalDebt - totalPaid;

  const entries: StatementEntry[] = [
    ...myTxns.map(t => ({
      date: t.Date,
      type: "txn" as const,
      label: `${t.Product_Name} × ${t.Quantity} وحدة`,
      amount: Number(t.Total_Price ?? 0),
    })),
    ...myPmts.map(p => ({
      date: (p as any).Date,
      type: "payment" as const,
      label: "دفعة مالية",
      amount: Number((p as any).Amount ?? 0),
    })),
  ].sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());

  let runningBalance = 0;

  return (
    <Modal
      title={`📋 كشف حساب: ${party.Name}`}
      onClose={onClose}
      maxWidth="max-w-[620px]"
      footer={
        <button onClick={onClose} className="erp-btn border border-slate-200 dark:border-slate-600 text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">
          إغلاق
        </button>
      }
    >
      {/* Summary row */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3 mb-5">
        <div className="rounded-xl bg-slate-50 dark:bg-slate-700/50 p-3 text-center">
          <p className="text-xs text-slate-400 mb-1">{isSupplier ? "إجمالي المشتريات" : "إجمالي المبيعات"}</p>
          <p className="font-700 text-slate-800 dark:text-slate-100 num text-sm">{fmtCurrency(totalDebt)} ج</p>
        </div>
        <div className="rounded-xl bg-emerald-50 dark:bg-emerald-900/20 p-3 text-center">
          <p className="text-xs text-slate-400 mb-1">المدفوع</p>
          <p className="font-700 text-emerald-600 dark:text-emerald-400 num text-sm">{fmtCurrency(totalPaid)} ج</p>
        </div>
        <div className={`rounded-xl p-3 text-center ${remaining > 0 ? "bg-red-50 dark:bg-red-900/20" : "bg-emerald-50 dark:bg-emerald-900/20"}`}>
          <p className="text-xs text-slate-400 mb-1">المتبقي</p>
          <p className={`font-700 num text-sm ${remaining > 0 ? "text-red-600 dark:text-red-400" : "text-emerald-600 dark:text-emerald-400"}`}>
            {fmtCurrency(remaining)} ج
          </p>
        </div>
      </div>

      <div className="divider" />

      {/* Entries */}
      {entries.length === 0 ? (
        <EmptyState label="لا توجد حركات لهذا الحساب" />
      ) : (
        <div className="space-y-1">
          <div className="grid grid-cols-1 sm:grid-cols-4 gap-2 px-2 py-1.5 text-xs font-700 text-slate-400 uppercase tracking-wide border-b border-slate-100 dark:border-slate-700">
            <span>التاريخ</span>
            <span className="col-span-1 sm:col-span-2">البيان</span>
            <span className="text-left">الرصيد</span>
          </div>
          {entries.map((e, i) => {
            if (e.type === "txn")     runningBalance += e.amount;
            if (e.type === "payment") runningBalance -= e.amount;
            return (
              <div
                key={i}
                className="grid grid-cols-1 sm:grid-cols-4 gap-2 px-2 py-2.5 rounded-lg hover:bg-slate-50 dark:hover:bg-slate-700/50 transition-colors animate-slide-up"
                style={{ animationDelay: `${i * 0.03}s` }}
              >
                <span className="text-xs text-slate-400">{fmtDate(e.date)}</span>
                <div className="col-span-2 flex items-center gap-2">
                  <span className={`badge text-xs ${e.type === "payment" ? "bg-emerald-100 text-emerald-700 dark:bg-emerald-900/40 dark:text-emerald-400" : "bg-blue-100 text-blue-700 dark:bg-blue-900/40 dark:text-blue-400"}`}>
                    {e.type === "payment" ? "💳 دفعة" : "🧾 فاتورة"}
                  </span>
                  <span className="text-sm text-slate-600 dark:text-slate-300">{e.label}</span>
                </div>
                <div className="text-left">
                  <span className={`font-700 num text-sm ${e.type === "payment" ? "text-emerald-500" : "text-red-500"}`}>
                    {e.type === "payment" ? "+" : "-"}{fmtCurrency(e.amount)} ج
                  </span>
                  <p className="text-xs text-slate-400 num mt-0.5">
                    رصيد: {fmtCurrency(Math.max(0, runningBalance))} ج
                  </p>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </Modal>
  );
}

// ─── Accounts Table ────────────────────────────────────────────────────────────
function AccountsTable({
  isSupplier, showToast,
}: {
  isSupplier: boolean;
  showToast: (m: string, t: "success" | "error") => void;
}) {
  const partiesSheet = isSupplier ? "Suppliers" : "Customers";
  const txnsSheet    = isSupplier ? "Supplier_Transactions" : "Customer_Transactions";
  const pmtsSheet    = isSupplier ? "Supplier_Payments" : "Customer_Payments";
  const partyIdKey   = isSupplier ? "Supplier_ID" : "Customer_ID";
  const partyNameKey = isSupplier ? "Supplier_Name" : "Customer_Name";

  const { data: parties,  isLoading: pL,  isError: pE,  refetch: pR  } = useSheet<Supplier | Customer>(partiesSheet);
  const { data: txns,     isLoading: tL,  isError: tE,  refetch: tR  } = useSheet<SupplierTransaction | CustomerTransaction>(txnsSheet);
  const { data: payments, isLoading: mL,  isError: mE,  refetch: mR  } = useSheet<SupplierPayment | CustomerPayment>(pmtsSheet);

  const [payModal,  setPayModal]  = useState<(Supplier | Customer) | null>(null);
  const [stmtModal, setStmtModal] = useState<(Supplier | Customer) | null>(null);
  const [payForm,   setPayForm]   = useState({ amount: "", date: todayStr() });
  const [saving,    setSaving]    = useState(false);

  const isLoading = pL || tL || mL;
  const isError   = pE || tE || mE;
  const refetchAll = () => { pR(); tR(); mR(); };

  if (isLoading) return <LoadingState />;
  if (isError)   return <ErrorState onRetry={refetchAll} />;

  // Compute account summary per party
  const accounts = (parties as (Supplier | Customer)[]).map(p => {
    const pid   = String(p.ID);
    const myTxns = (txns  as any[]).filter(t => String(t[partyIdKey]) === pid);
    const myPmts = (payments as any[]).filter(x => String(x[partyIdKey]) === pid);
    const debt      = myTxns.reduce((s: number, t: any) => s + Number(t.Total_Price ?? 0), 0);
    const paid      = myPmts.reduce((s: number, x: any) => s + Number(x.Amount ?? 0), 0);
    return { ...p, debt, paid, remaining: debt - paid };
  });

  const totalDebt = accounts.reduce((s, a) => s + a.debt, 0);
  const totalPaid = accounts.reduce((s, a) => s + a.paid, 0);
  const totalRem  = accounts.reduce((s, a) => s + a.remaining, 0);

  // Payment — find account for current payModal
  const payAccount = payModal ? accounts.find(a => a.ID === payModal.ID) : null;

  const handlePayment = async () => {
    if (!payForm.amount || !payModal) return;
    setSaving(true);
    try {
      await postSheet(pmtsSheet, {
        ID:              maxId(payments as { ID: number }[]),
        [partyIdKey]:    payModal.ID,
        [partyNameKey]:  payModal.Name,
        Amount:          Number(payForm.amount),
        Date:            payForm.date,
      });
      showToast("تم تسجيل الدفعة بنجاح ✓", "success");
      setPayModal(null);
      setPayForm({ amount: "", date: todayStr() });
      setTimeout(mR, 1200);
    } catch {
      showToast("فشل في الحفظ، أعد المحاولة", "error");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="animate-fade-in space-y-5">
      {/* Summary stats */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 stagger-children">
        <StatCard
          label={isSupplier ? "إجمالي الذمم للموردين" : "إجمالي مبيعات العملاء"}
          value={`${fmtCurrency(totalDebt)} ج`}
          icon={isSupplier ? <Truck size={20}/> : <TrendingUp size={20}/>}
          color="blue"
        />
        <StatCard
          label="إجمالي المدفوع"
          value={`${fmtCurrency(totalPaid)} ج`}
          icon={<DollarSign size={20}/>}
          color="green"
        />
        <StatCard
          label="إجمالي المتبقي"
          value={`${fmtCurrency(totalRem)} ج`}
          icon={totalRem > 0 ? <TrendingDown size={20}/> : <TrendingUp size={20}/>}
          color={totalRem > 0 ? "red" : "green"}
        />
      </div>

      {/* Accounts table */}
      <div>
        <SectionHeader
          title={isSupplier ? "حسابات الموردين" : "حسابات العملاء"}
          count={accounts.length}
        />
        <div className="erp-card overflow-hidden">
          <div className="overflow-x-auto">
            <table className="erp-table">
              <thead>
                <tr>
                  <th>الكود</th>
                  <th>الاسم</th>
                  <th>{isSupplier ? "إجمالي المشتريات" : "إجمالي المبيعات"}</th>
                  <th>المدفوع</th>
                  <th>المتبقي</th>
                  <th>الحالة</th>
                  <th>إجراءات</th>
                </tr>
              </thead>
              <tbody>
                {accounts.length === 0 ? (
                  <tr><td colSpan={7}><EmptyState label={isSupplier ? "لا يوجد موردون مسجّلون" : "لا يوجد عملاء مسجّلون"} /></td></tr>
                ) : accounts.map((a, i) => {
                  const settledPct = a.debt > 0 ? (a.paid / a.debt) * 100 : 100;
                  const status =
                    a.remaining <= 0            ? { label: "مسدّد بالكامل ✓", variant: "green" as const } :
                    settledPct >= 50            ? { label: "نصف مسدّد",         variant: "yellow" as const } :
                                                  { label: "مديونية",           variant: "red" as const };
                  return (
                    <tr key={a.ID} className="animate-slide-up" style={{ animationDelay: `${i * 0.04}s` }}>
                      <td><Badge variant={isSupplier ? "blue" : "purple"}>#{a.ID}</Badge></td>
                      <td className="font-700 text-slate-800 dark:text-slate-100">{a.Name}</td>
                      <td className="num text-slate-600 dark:text-slate-300">{fmtCurrency(a.debt)} ج</td>
                      <td className="num text-emerald-600 dark:text-emerald-400 font-600">{fmtCurrency(a.paid)} ج</td>
                      <td className={`num font-700 ${a.remaining > 0 ? "text-red-600 dark:text-red-400" : "text-emerald-600 dark:text-emerald-400"}`}>
                        {fmtCurrency(a.remaining)} ج
                      </td>
                      <td>
                        <div className="flex flex-col gap-1">
                          <Badge variant={status.variant}>{status.label}</Badge>
                          {/* Progress bar */}
                          {a.debt > 0 && (
                            <div className="w-20 h-1.5 bg-slate-200 dark:bg-slate-700 rounded-full overflow-hidden">
                              <div
                                className={`h-full rounded-full transition-all ${settledPct >= 100 ? "bg-emerald-500" : settledPct >= 50 ? "bg-amber-400" : "bg-red-400"}`}
                                style={{ width: `${Math.min(100, settledPct)}%` }}
                              />
                            </div>
                          )}
                        </div>
                      </td>
                      <td>
                        <div className="flex items-center gap-2">
                          <div className="flex flex-wrap justify-end items-center gap-2">
                          {getPhoneHref(a.Phone) ? (
                            <a href={getPhoneHref(a.Phone)} className="contact-action call" aria-label={`اتصل بـ ${a.Name}`}>
                              <Phone size={14} /> اتصال
                            </a>
                          ) : (
                            <button disabled className="contact-action call disabled" aria-label="لا يوجد رقم هاتف متاح">
                              <Phone size={14} /> اتصال
                            </button>
                          )}
                          {getWhatsappHref(a.Phone) ? (
                            <a href={getWhatsappHref(a.Phone)} target="_blank" rel="noreferrer" className="contact-action whatsapp" aria-label={`أرسل رسالة واتساب إلى ${a.Name}`}>
                              <MessageSquare size={14} /> واتساب
                            </a>
                          ) : (
                            <button disabled className="contact-action whatsapp disabled" aria-label="لا يوجد رقم هاتف متاح">
                              <MessageSquare size={14} /> واتساب
                            </button>
                          )}
                          <button
                          onClick={() => { setPayModal(a); setPayForm({ amount: "", date: todayStr() }); }}
                          className="erp-btn bg-emerald-500 hover:bg-emerald-600 text-white text-xs px-3 py-1.5"
                        >
                          <CreditCard size={13} /> دفعة
                        </button>
                        <button
                          onClick={() => setStmtModal(a)}
                          className="erp-btn border border-slate-200 dark:border-slate-600 text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 text-xs px-3 py-1.5"
                        >
                          <FileText size={13} /> كشف
                        </button>
                      </div>
                    </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Payment Modal */}
      {payModal && (
        <Modal
          title={`💳 إضافة دفعة — ${payModal.Name}`}
          onClose={() => setPayModal(null)}
          footer={
            <>
              <button onClick={() => setPayModal(null)} className="erp-btn border border-slate-200 dark:border-slate-600 text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">
                إلغاء
              </button>
              <button
                onClick={handlePayment}
                disabled={saving || !payForm.amount}
                className="erp-btn bg-emerald-500 hover:bg-emerald-600 text-white disabled:opacity-50"
              >
                {saving ? "جاري الحفظ..." : "✓ تسجيل الدفعة"}
              </button>
            </>
          }
        >
          {/* Balance summary */}
          {payAccount && (
            <div className="rounded-xl bg-red-50 dark:bg-red-900/20 p-4 mb-4 flex items-center justify-between">
              <div>
                <p className="text-xs text-slate-500 dark:text-slate-400 mb-0.5">الرصيد المتبقي</p>
                <p className="text-2xl font-800 text-red-600 dark:text-red-400 num">{fmtCurrency(payAccount.remaining)} ج</p>
              </div>
              <div className="text-right text-xs text-slate-400 space-y-1">
                <p>إجمالي: <span className="font-600 text-slate-600 dark:text-slate-300">{fmtCurrency(payAccount.debt)} ج</span></p>
                <p>مدفوع: <span className="font-600 text-emerald-600">{fmtCurrency(payAccount.paid)} ج</span></p>
              </div>
            </div>
          )}
          <div className="space-y-4">
            <FormGroup label="المبلغ المدفوع (ج)" required>
              <input
                className="erp-input text-lg font-600"
                type="number"
                min="0"
                step="0.01"
                value={payForm.amount}
                onChange={e => setPayForm({ ...payForm, amount: e.target.value })}
                placeholder="0.00"
                autoFocus
              />
              {payAccount && payForm.amount && (
                <p className="text-xs mt-1 text-slate-400">
                  الرصيد بعد الدفع:{" "}
                  <span className={`font-600 ${payAccount.remaining - Number(payForm.amount) <= 0 ? "text-emerald-600" : "text-red-500"}`}>
                    {fmtCurrency(Math.max(0, payAccount.remaining - Number(payForm.amount)))} ج
                  </span>
                </p>
              )}
            </FormGroup>
            <FormGroup label="تاريخ الدفع">
              <input
                className="erp-input"
                type="date"
                value={payForm.date}
                onChange={e => setPayForm({ ...payForm, date: e.target.value })}
              />
            </FormGroup>
          </div>
        </Modal>
      )}

      {/* Statement Modal */}
      {stmtModal && (
        <AccountStatementModal
          party={stmtModal}
          txns={txns as (SupplierTransaction | CustomerTransaction)[]}
          payments={payments as (SupplierPayment | CustomerPayment)[]}
          isSupplier={isSupplier}
          onClose={() => setStmtModal(null)}
        />
      )}
    </div>
  );
}

// ─── Finance Module ────────────────────────────────────────────────────────────
export default function FinanceModule({
  showToast,
}: {
  showToast: (m: string, t: "success" | "error") => void;
}) {
  const [tab, setTab] = useState("suppliers");
  const tabs = [
    { id: "suppliers", label: "حسابات الموردين", icon: <Truck size={15} /> },
    { id: "customers", label: "حسابات العملاء",  icon: <Users size={15} /> },
  ];
  return (
    <div>
      <TabBar tabs={tabs} active={tab} onChange={setTab} />
      {tab === "suppliers" && <AccountsTable isSupplier={true}  showToast={showToast} key="sf" />}
      {tab === "customers" && <AccountsTable isSupplier={false} showToast={showToast} key="cf" />}
    </div>
  );
}
