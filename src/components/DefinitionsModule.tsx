"use client";
import React, { useState } from "react";
import { Plus, Truck, Users, Package, Tag, Phone, MessageSquare, Pencil, Trash2, AlertTriangle } from "lucide-react";
import { useSheet } from "@/hooks/useSheet";
import { Supplier, Customer, Product, postSheet, updateSheet, deleteSheet, renumberSheet, validateEgPhone, maxId, fmtCurrency } from "@/lib/api";
import {
  LoadingState, ErrorState, EmptyState,
  Modal, FormGroup, SectionHeader, TabBar, Badge,
} from "@/components/ui";

const normalizePhone = (phone?: string | number | null) => String(phone ?? "").replace(/\D/g, "");
const getPhoneHref   = (phone?: string | number | null) => { const d = normalizePhone(phone); return d ? `tel:${d}` : undefined; };
const getWhatsappHref = (phone?: string | number | null) => {
  const d = normalizePhone(phone);
  if (!d) return undefined;
  return `https://wa.me/${d.startsWith("0") ? `2${d.slice(1)}` : d}`;
};

// ─── Confirm Delete Dialog ────────────────────────────────────────────────────
function ConfirmDelete({ name, onConfirm, onCancel, loading }: {
  name: string; onConfirm: () => void; onCancel: () => void; loading: boolean;
}) {
  return (
    <Modal title="تأكيد الحذف" onClose={onCancel} footer={
      <>
        <button onClick={onCancel} disabled={loading}
          className="erp-btn border border-slate-200 dark:border-slate-600 text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">
          إلغاء
        </button>
        <button onClick={onConfirm} disabled={loading}
          className="erp-btn bg-red-500 hover:bg-red-600 text-white disabled:opacity-50">
          {loading ? "جارٍ الحذف..." : "🗑️ تأكيد الحذف"}
        </button>
      </>
    } maxWidth="max-w-[400px]">
      <div className="flex items-start gap-3 rounded-xl bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 p-4">
        <AlertTriangle size={18} className="text-red-500 shrink-0 mt-0.5" />
        <p className="text-sm text-slate-700 dark:text-slate-200 leading-relaxed">
          هل أنت متأكد من حذف <strong>"{name}"</strong>؟ لا يمكن التراجع عن هذا الإجراء.
        </p>
      </div>
    </Modal>
  );
}

// ─── Suppliers Tab ─────────────────────────────────────────────────────────────
function SuppliersTab({ showToast }: { showToast: (m: string, t: "success" | "error") => void }) {
  const { data, isLoading, isError, refetch } = useSheet<Supplier>("Suppliers");
  const [open,      setOpen]      = useState(false);
  const [editRow,   setEditRow]   = useState<Supplier | null>(null);
  const [deleteRow, setDeleteRow] = useState<Supplier | null>(null);
  const [saving,    setSaving]    = useState(false);
  const [form,      setForm]      = useState({ Name: "", Phone: "", Address: "" });
  const phoneErr = validateEgPhone(form.Phone);

  if (isLoading) return <LoadingState />;
  if (isError)   return <ErrorState onRetry={refetch} />;

  const openAdd  = () => { setForm({ Name: "", Phone: "", Address: "" }); setOpen(true); };
  const openEdit = (r: Supplier) => { setForm({ Name: r.Name, Phone: r.Phone ?? "", Address: r.Address ?? "" }); setEditRow(r); };

  const handleAdd = async () => {
    if (!form.Name.trim()) return;
    setSaving(true);
    try {
      await postSheet("Suppliers", { ID: maxId(data), ...form });
      showToast("تمت إضافة المورد بنجاح ✓", "success");
      setOpen(false); setForm({ Name: "", Phone: "", Address: "" });
      setTimeout(refetch, 1200);
    } catch { showToast("فشل في الحفظ، أعد المحاولة", "error"); }
    finally { setSaving(false); }
  };

  const handleEdit = async () => {
    if (!editRow || !form.Name.trim()) return;
    setSaving(true);
    try {
      await updateSheet("Suppliers", { ID: editRow.ID, ...form });
      showToast("تم تحديث بيانات المورد ✓", "success");
      setEditRow(null); setTimeout(refetch, 1200);
    } catch { showToast("فشل في الحفظ، أعد المحاولة", "error"); }
    finally { setSaving(false); }
  };

  const handleDelete = async () => {
    if (!deleteRow) return;
    setSaving(true);
    try {
      await deleteSheet("Suppliers", deleteRow.ID);
      // Re-sequence IDs so they stay 1,2,3... and update foreign keys
      await renumberSheet("Suppliers", "Supplier_ID", ["Supplier_Transactions", "Supplier_Payments"]);
      showToast("تم حذف المورد وإعادة ترتيب الأرقام ✓", "success");
      setDeleteRow(null); setTimeout(refetch, 1500);
    } catch { showToast("فشل في الحذف، أعد المحاولة", "error"); }
    finally { setSaving(false); }
  };

  const ModalForm = (
    <div className="space-y-4">
      <FormGroup label="اسم المورد" required>
        <input className="erp-input" value={form.Name} onChange={e => setForm({ ...form, Name: e.target.value })}
          placeholder="مثال: شركة المتحدة للتوريد" autoFocus />
      </FormGroup>
      <FormGroup label="رقم الهاتف" hint="صيغة مصرية: 01XXXXXXXXX">
        <input
          className={`erp-input ${phoneErr ? "border-red-400 focus:ring-red-300" : ""}`}
          value={form.Phone}
          onChange={e => setForm({ ...form, Phone: e.target.value })}
          placeholder="01007809902"
          type="tel"
          inputMode="tel"
          maxLength={11}
          dir="ltr"
        />
        {phoneErr && <p className="text-red-500 text-xs mt-1">{phoneErr}</p>}
      </FormGroup>
      <FormGroup label="العنوان">
        <input className="erp-input" value={form.Address} onChange={e => setForm({ ...form, Address: e.target.value })} placeholder="المدينة، الشارع..." />
      </FormGroup>
    </div>
  );

  return (
    <div className="animate-fade-in">
      <SectionHeader title="الموردون" count={data.length}
        action={<button onClick={openAdd} className="erp-btn bg-blue-500 hover:bg-blue-600 text-white"><Plus size={16} /> إضافة مورد</button>} />
      <div className="erp-card overflow-hidden">
        <div className="overflow-x-auto">
          <table className="erp-table">
            <thead><tr><th>الكود</th><th>الاسم</th><th>الهاتف</th><th>التواصل</th><th>العنوان</th><th></th></tr></thead>
            <tbody>
              {data.length === 0 ? (
                <tr><td colSpan={6}><EmptyState label="لا يوجد موردون مسجّلون بعد" /></td></tr>
              ) : data.map((r, i) => (
                <tr key={r.ID} style={{ animationDelay: `${i * 0.04}s` }} className="animate-slide-up">
                  <td><Badge variant="blue">#{r.ID}</Badge></td>
                  <td className="font-700 text-slate-800 dark:text-slate-100">{r.Name}</td>
                  <td className="text-slate-500 dark:text-slate-400">{r.Phone || "—"}</td>
                  <td>
                    <div className="flex flex-wrap items-center gap-1.5">
                      {getPhoneHref(r.Phone)
                        ? <a href={getPhoneHref(r.Phone)} className="contact-action call"><Phone size={13} /> اتصال</a>
                        : <button disabled className="contact-action call disabled"><Phone size={13} /> اتصال</button>}
                      {getWhatsappHref(r.Phone)
                        ? <a href={getWhatsappHref(r.Phone)} target="_blank" rel="noreferrer" className="contact-action whatsapp"><MessageSquare size={13} /> واتساب</a>
                        : <button disabled className="contact-action whatsapp disabled"><MessageSquare size={13} /> واتساب</button>}
                    </div>
                  </td>
                  <td className="text-slate-500 dark:text-slate-400 text-sm">{r.Address || "—"}</td>
                  <td>
                    <div className="flex items-center gap-1 justify-end">
                      <button onClick={() => openEdit(r)} title="تعديل"
                        className="erp-btn px-2 py-1.5 text-slate-500 hover:text-blue-600 hover:bg-blue-50 dark:hover:bg-blue-900/20 border border-transparent hover:border-blue-200 dark:hover:border-blue-800">
                        <Pencil size={13} />
                      </button>
                      <button onClick={() => setDeleteRow(r)} title="حذف"
                        className="erp-btn px-2 py-1.5 text-slate-500 hover:text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20 border border-transparent hover:border-red-200 dark:hover:border-red-800">
                        <Trash2 size={13} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {open && (
        <Modal title="إضافة مورد جديد" onClose={() => setOpen(false)} footer={
          <><button onClick={() => setOpen(false)} className="erp-btn border border-slate-200 dark:border-slate-600 text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">إلغاء</button>
          <button onClick={handleAdd} disabled={saving || !form.Name.trim() || !!phoneErr} className="erp-btn bg-blue-500 hover:bg-blue-600 text-white disabled:opacity-50">{saving ? "جاري الحفظ..." : "💾 حفظ"}</button></>
        }>{ModalForm}</Modal>
      )}
      {editRow && (
        <Modal title={`تعديل بيانات: ${editRow.Name}`} onClose={() => setEditRow(null)} footer={
          <><button onClick={() => setEditRow(null)} className="erp-btn border border-slate-200 dark:border-slate-600 text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">إلغاء</button>
          <button onClick={handleEdit} disabled={saving || !form.Name.trim() || !!phoneErr} className="erp-btn bg-blue-500 hover:bg-blue-600 text-white disabled:opacity-50">{saving ? "جاري الحفظ..." : "✏️ حفظ التعديلات"}</button></>
        }>{ModalForm}</Modal>
      )}
      {deleteRow && <ConfirmDelete name={deleteRow.Name} loading={saving} onConfirm={handleDelete} onCancel={() => setDeleteRow(null)} />}
    </div>
  );
}

// ─── Customers Tab ─────────────────────────────────────────────────────────────
function CustomersTab({ showToast }: { showToast: (m: string, t: "success" | "error") => void }) {
  const { data, isLoading, isError, refetch } = useSheet<Customer>("Customers");
  const [open,      setOpen]      = useState(false);
  const [editRow,   setEditRow]   = useState<Customer | null>(null);
  const [deleteRow, setDeleteRow] = useState<Customer | null>(null);
  const [saving,    setSaving]    = useState(false);
  const [form,      setForm]      = useState({ Name: "", Phone: "", Address: "" });
  const phoneErr = validateEgPhone(form.Phone);

  if (isLoading) return <LoadingState />;
  if (isError)   return <ErrorState onRetry={refetch} />;

  const openAdd  = () => { setForm({ Name: "", Phone: "", Address: "" }); setOpen(true); };
  const openEdit = (r: Customer) => { setForm({ Name: r.Name, Phone: r.Phone ?? "", Address: r.Address ?? "" }); setEditRow(r); };

  const handleAdd = async () => {
    if (!form.Name.trim()) return;
    setSaving(true);
    try {
      await postSheet("Customers", { ID: maxId(data), ...form });
      showToast("تمت إضافة العميل بنجاح ✓", "success");
      setOpen(false); setForm({ Name: "", Phone: "", Address: "" });
      setTimeout(refetch, 1200);
    } catch { showToast("فشل في الحفظ، أعد المحاولة", "error"); }
    finally { setSaving(false); }
  };

  const handleEdit = async () => {
    if (!editRow || !form.Name.trim()) return;
    setSaving(true);
    try {
      await updateSheet("Customers", { ID: editRow.ID, ...form });
      showToast("تم تحديث بيانات العميل ✓", "success");
      setEditRow(null); setTimeout(refetch, 1200);
    } catch { showToast("فشل في الحفظ، أعد المحاولة", "error"); }
    finally { setSaving(false); }
  };

  const handleDelete = async () => {
    if (!deleteRow) return;
    setSaving(true);
    try {
      await deleteSheet("Customers", deleteRow.ID);
      await renumberSheet("Customers", "Customer_ID", ["Customer_Transactions", "Customer_Payments"]);
      showToast("تم حذف العميل وإعادة ترتيب الأرقام ✓", "success");
      setDeleteRow(null); setTimeout(refetch, 1500);
    } catch { showToast("فشل في الحذف، أعد المحاولة", "error"); }
    finally { setSaving(false); }
  };

  const ModalForm = (
    <div className="space-y-4">
      <FormGroup label="اسم العميل" required>
        <input className="erp-input" value={form.Name} onChange={e => setForm({ ...form, Name: e.target.value })} placeholder="اسم العميل الكامل" autoFocus />
      </FormGroup>
      <FormGroup label="رقم الهاتف" hint="صيغة مصرية: 01XXXXXXXXX">
        <input
          className={`erp-input ${phoneErr ? "border-red-400 focus:ring-red-300" : ""}`}
          value={form.Phone}
          onChange={e => setForm({ ...form, Phone: e.target.value })}
          placeholder="01007809902"
          type="tel"
          inputMode="tel"
          maxLength={11}
          dir="ltr"
        />
        {phoneErr && <p className="text-red-500 text-xs mt-1">{phoneErr}</p>}
      </FormGroup>
      <FormGroup label="العنوان">
        <input className="erp-input" value={form.Address} onChange={e => setForm({ ...form, Address: e.target.value })} placeholder="المدينة، الشارع..." />
      </FormGroup>
    </div>
  );

  return (
    <div className="animate-fade-in">
      <SectionHeader title="العملاء" count={data.length}
        action={<button onClick={openAdd} className="erp-btn bg-purple-500 hover:bg-purple-600 text-white"><Plus size={16} /> إضافة عميل</button>} />
      <div className="erp-card overflow-hidden">
        <div className="overflow-x-auto">
          <table className="erp-table">
            <thead><tr><th>الكود</th><th>الاسم</th><th>الهاتف</th><th>التواصل</th><th>العنوان</th><th></th></tr></thead>
            <tbody>
              {data.length === 0 ? (
                <tr><td colSpan={6}><EmptyState label="لا يوجد عملاء مسجّلون بعد" /></td></tr>
              ) : data.map((r, i) => (
                <tr key={r.ID} style={{ animationDelay: `${i * 0.04}s` }} className="animate-slide-up">
                  <td><Badge variant="purple">#{r.ID}</Badge></td>
                  <td className="font-700 text-slate-800 dark:text-slate-100">{r.Name}</td>
                  <td className="text-slate-500 dark:text-slate-400">{r.Phone || "—"}</td>
                  <td>
                    <div className="flex flex-wrap items-center gap-1.5">
                      {getPhoneHref(r.Phone)
                        ? <a href={getPhoneHref(r.Phone)} className="contact-action call"><Phone size={13} /> اتصال</a>
                        : <button disabled className="contact-action call disabled"><Phone size={13} /> اتصال</button>}
                      {getWhatsappHref(r.Phone)
                        ? <a href={getWhatsappHref(r.Phone)} target="_blank" rel="noreferrer" className="contact-action whatsapp"><MessageSquare size={13} /> واتساب</a>
                        : <button disabled className="contact-action whatsapp disabled"><MessageSquare size={13} /> واتساب</button>}
                    </div>
                  </td>
                  <td className="text-slate-500 dark:text-slate-400 text-sm">{r.Address || "—"}</td>
                  <td>
                    <div className="flex items-center gap-1 justify-end">
                      <button onClick={() => openEdit(r)} title="تعديل"
                        className="erp-btn px-2 py-1.5 text-slate-500 hover:text-purple-600 hover:bg-purple-50 dark:hover:bg-purple-900/20 border border-transparent hover:border-purple-200 dark:hover:border-purple-800">
                        <Pencil size={13} />
                      </button>
                      <button onClick={() => setDeleteRow(r)} title="حذف"
                        className="erp-btn px-2 py-1.5 text-slate-500 hover:text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20 border border-transparent hover:border-red-200 dark:hover:border-red-800">
                        <Trash2 size={13} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {open && (
        <Modal title="إضافة عميل جديد" onClose={() => setOpen(false)} footer={
          <><button onClick={() => setOpen(false)} className="erp-btn border border-slate-200 dark:border-slate-600 text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">إلغاء</button>
          <button onClick={handleAdd} disabled={saving || !form.Name.trim() || !!phoneErr} className="erp-btn bg-purple-500 hover:bg-purple-600 text-white disabled:opacity-50">{saving ? "جاري الحفظ..." : "💾 حفظ"}</button></>
        }>{ModalForm}</Modal>
      )}
      {editRow && (
        <Modal title={`تعديل بيانات: ${editRow.Name}`} onClose={() => setEditRow(null)} footer={
          <><button onClick={() => setEditRow(null)} className="erp-btn border border-slate-200 dark:border-slate-600 text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">إلغاء</button>
          <button onClick={handleEdit} disabled={saving || !form.Name.trim() || !!phoneErr} className="erp-btn bg-purple-500 hover:bg-purple-600 text-white disabled:opacity-50">{saving ? "جاري الحفظ..." : "✏️ حفظ التعديلات"}</button></>
        }>{ModalForm}</Modal>
      )}
      {deleteRow && <ConfirmDelete name={deleteRow.Name} loading={saving} onConfirm={handleDelete} onCancel={() => setDeleteRow(null)} />}
    </div>
  );
}

// ─── Products Tab ──────────────────────────────────────────────────────────────
function ProductsTab({ showToast }: { showToast: (m: string, t: "success" | "error") => void }) {
  const { data, isLoading, isError, refetch } = useSheet<Product>("Products");
  const [open,      setOpen]      = useState(false);
  const [editRow,   setEditRow]   = useState<Product | null>(null);
  const [deleteRow, setDeleteRow] = useState<Product | null>(null);
  const [saving,    setSaving]    = useState(false);
  const [form,      setForm]      = useState({ Name: "", Quantity: "", Purchase_Price: "" });

  if (isLoading) return <LoadingState />;
  if (isError)   return <ErrorState onRetry={refetch} />;

  const openAdd  = () => { setForm({ Name: "", Quantity: "", Purchase_Price: "" }); setOpen(true); };
  const openEdit = (r: Product) => {
    setForm({
      Name:           r.Name,
      Quantity:       String(r.Quantity       ?? ""),
      Purchase_Price: String(r.Purchase_Price ?? ""),
    });
    setEditRow(r);
  };

  const handleAdd = async () => {
    if (!form.Name.trim()) return;
    setSaving(true);
    try {
      await postSheet("Products", {
        ID:             maxId(data),
        Name:           form.Name.trim(),
        Quantity:       Number(form.Quantity)       || 0,
        Purchase_Price: Number(form.Purchase_Price) || 0,
      });
      showToast("تمت إضافة المنتج بنجاح ✓", "success");
      setOpen(false);
      setForm({ Name: "", Quantity: "", Purchase_Price: "" });
      setTimeout(refetch, 1200);
    } catch { showToast("فشل في الحفظ، أعد المحاولة", "error"); }
    finally { setSaving(false); }
  };

  const handleEdit = async () => {
    if (!editRow || !form.Name.trim()) return;
    setSaving(true);
    try {
      await updateSheet("Products", {
        ID:             editRow.ID,
        Name:           form.Name.trim(),
        Quantity:       Number(form.Quantity)       || 0,
        Purchase_Price: Number(form.Purchase_Price) || 0,
      });
      showToast("تم تحديث بيانات المنتج ✓", "success");
      setEditRow(null);
      setTimeout(refetch, 1200);
    } catch { showToast("فشل في الحفظ، أعد المحاولة", "error"); }
    finally { setSaving(false); }
  };

  const handleDelete = async () => {
    if (!deleteRow) return;
    setSaving(true);
    try {
      await deleteSheet("Products", deleteRow.ID);
      await renumberSheet("Products", "Product_ID", ["Supplier_Transactions", "Customer_Transactions", "Inventory_Adjustments"]);
      showToast("تم حذف المنتج وإعادة ترتيب الأرقام ✓", "success");
      setDeleteRow(null);
      setTimeout(refetch, 1500);
    } catch { showToast("فشل في الحذف، أعد المحاولة", "error"); }
    finally { setSaving(false); }
  };

  const ModalForm = (
    <div className="space-y-4">
      <FormGroup label="اسم المنتج" required>
        <input className="erp-input" value={form.Name}
          onChange={e => setForm({ ...form, Name: e.target.value })}
          placeholder="مثال: سكر 50 كيلو" autoFocus />
      </FormGroup>
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
        <FormGroup label="الكمية الأولية" hint="الكمية الموجودة الآن (اختياري)">
          <input className="erp-input" type="number" min="0" step="1"
            value={form.Quantity}
            onChange={e => setForm({ ...form, Quantity: e.target.value })}
            placeholder="0" />
        </FormGroup>
        <FormGroup label="سعر الشراء (ج)" hint="التكلفة عند الشراء (اختياري)">
          <input className="erp-input" type="number" min="0" step="0.01"
            value={form.Purchase_Price}
            onChange={e => setForm({ ...form, Purchase_Price: e.target.value })}
            placeholder="0.00" />
        </FormGroup>
      </div>
      <p className="text-xs text-slate-400 dark:text-slate-500">
        💡 سعر البيع يُحدَّد وقت إنشاء كل فاتورة بيع — يختلف من عميل لآخر.
      </p>
    </div>
  );

  return (
    <div className="animate-fade-in">
      <SectionHeader title="المنتجات" count={data.length}
        action={<button onClick={openAdd} className="erp-btn bg-amber-500 hover:bg-amber-600 text-white"><Plus size={16} /> إضافة منتج</button>} />
      <div className="erp-card overflow-hidden">
        <div className="overflow-x-auto">
          <table className="erp-table">
            <thead>
              <tr>
                <th>الكود</th>
                <th>اسم المنتج</th>
                <th className="text-center">الكمية الأولية</th>
                <th className="text-center">سعر الشراء</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {data.length === 0 ? (
                <tr><td colSpan={5}><EmptyState label="لا توجد منتجات مسجّلة بعد" /></td></tr>
              ) : data.map((r, i) => (
                <tr key={r.ID} className="animate-slide-up" style={{ animationDelay: `${i * 0.04}s` }}>
                  <td><Badge variant="yellow">#{r.ID}</Badge></td>
                  <td>
                    <div className="flex items-center gap-2">
                      <div className="w-7 h-7 rounded-lg bg-amber-100 dark:bg-amber-900/30 flex items-center justify-center">
                        <Tag size={13} className="text-amber-600 dark:text-amber-400" />
                      </div>
                      <span className="font-600 text-slate-800 dark:text-slate-100">{r.Name}</span>
                    </div>
                  </td>
                  <td className="text-center num text-slate-600 dark:text-slate-300">
                    {Number(r.Quantity) > 0 ? Number(r.Quantity) : <span className="text-slate-400">—</span>}
                  </td>
                  <td className="text-center num text-slate-600 dark:text-slate-300">
                    {Number(r.Purchase_Price) > 0 ? `${fmtCurrency(r.Purchase_Price)} ج` : <span className="text-slate-400">—</span>}
                  </td>
                  <td>
                    <div className="flex items-center gap-1 justify-end">
                      <button onClick={() => openEdit(r)} title="تعديل"
                        className="erp-btn px-2 py-1.5 text-slate-500 hover:text-amber-600 hover:bg-amber-50 dark:hover:bg-amber-900/20 border border-transparent hover:border-amber-200 dark:hover:border-amber-800">
                        <Pencil size={13} />
                      </button>
                      <button onClick={() => setDeleteRow(r)} title="حذف"
                        className="erp-btn px-2 py-1.5 text-slate-500 hover:text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20 border border-transparent hover:border-red-200 dark:hover:border-red-800">
                        <Trash2 size={13} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {open && (
        <Modal title="إضافة منتج جديد" onClose={() => setOpen(false)} footer={
          <><button onClick={() => setOpen(false)} className="erp-btn border border-slate-200 dark:border-slate-600 text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">إلغاء</button>
          <button onClick={handleAdd} disabled={saving || !form.Name.trim()} className="erp-btn bg-amber-500 hover:bg-amber-600 text-white disabled:opacity-50">{saving ? "جاري الحفظ..." : "💾 حفظ"}</button></>
        }>{ModalForm}</Modal>
      )}
      {editRow && (
        <Modal title={`تعديل بيانات: ${editRow.Name}`} onClose={() => setEditRow(null)} footer={
          <><button onClick={() => setEditRow(null)} className="erp-btn border border-slate-200 dark:border-slate-600 text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">إلغاء</button>
          <button onClick={handleEdit} disabled={saving || !form.Name.trim()} className="erp-btn bg-amber-500 hover:bg-amber-600 text-white disabled:opacity-50">{saving ? "جاري الحفظ..." : "✏️ حفظ التعديلات"}</button></>
        }>{ModalForm}</Modal>
      )}
      {deleteRow && <ConfirmDelete name={deleteRow.Name} loading={saving} onConfirm={handleDelete} onCancel={() => setDeleteRow(null)} />}
    </div>
  );
}

// ─── Definitions Module ────────────────────────────────────────────────────────
export default function DefinitionsModule({ showToast }: { showToast: (m: string, t: "success" | "error") => void }) {
  const [tab, setTab] = useState("suppliers");
  const tabs = [
    { id: "suppliers", label: "الموردون", icon: <Truck size={15} /> },
    { id: "customers", label: "العملاء",  icon: <Users size={15} /> },
    { id: "products",  label: "المنتجات", icon: <Package size={15} /> },
  ];
  return (
    <div>
      <TabBar tabs={tabs} active={tab} onChange={setTab} />
      {tab === "suppliers" && <SuppliersTab showToast={showToast} />}
      {tab === "customers" && <CustomersTab showToast={showToast} />}
      {tab === "products"  && <ProductsTab  showToast={showToast} />}
    </div>
  );
}
