"use client";
import React, { useEffect, useRef } from "react";
import {
  AlertCircle, RefreshCw, Loader2, X, CheckCircle2, XCircle,
  InboxIcon,
} from "lucide-react";

// ─── Loading Skeleton ──────────────────────────────────────────────────────────
export function LoadingState({ label = "جاري تحميل البيانات..." }: { label?: string }) {
  return (
    <div className="flex flex-col items-center justify-center py-20 gap-5 animate-fade-in">
      {/* Animated rings */}
      <div className="relative w-16 h-16">
        <div className="absolute inset-0 rounded-full border-4 border-blue-100 dark:border-slate-700" />
        <div className="absolute inset-0 rounded-full border-4 border-transparent border-t-blue-500 animate-spin-erp" />
        <div
          className="absolute inset-2 rounded-full border-4 border-transparent border-t-blue-300 animate-spin-erp"
          style={{ animationDuration: "0.7s", animationDirection: "reverse" }}
        />
      </div>
      <p className="text-slate-500 dark:text-slate-400 text-sm font-500">{label}</p>
      {/* Shimmer rows */}
      <div className="w-full max-w-lg space-y-3 px-4">
        {[100, 85, 92, 78].map((w, i) => (
          <div
            key={i}
            className="h-10 rounded-xl bg-slate-200 dark:bg-slate-700 animate-pulse"
            style={{ width: `${w}%`, animationDelay: `${i * 0.1}s` }}
          />
        ))}
      </div>
    </div>
  );
}

// ─── Error State ───────────────────────────────────────────────────────────────
export function ErrorState({ onRetry }: { onRetry: () => void }) {
  return (
    <div className="flex flex-col items-center justify-center py-16 gap-4 animate-fade-in text-center">
      <div className="w-16 h-16 rounded-full bg-red-100 dark:bg-red-900/30 flex items-center justify-center">
        <AlertCircle className="w-8 h-8 text-red-500" />
      </div>
      <div>
        <p className="text-base font-700 text-slate-800 dark:text-slate-100 mb-1">
          حدث خطأ في تحميل البيانات
        </p>
        <p className="text-sm text-slate-500 dark:text-slate-400 max-w-xs mx-auto">
          تعذّر الاتصال بالخادم. تحقق من اتصالك بالإنترنت ثم أعد المحاولة.
        </p>
      </div>
      <button
        onClick={onRetry}
        className="erp-btn mt-1 bg-blue-500 hover:bg-blue-600 text-white gap-2 px-5 py-2.5"
      >
        <RefreshCw className="w-4 h-4" />
        إعادة المحاولة
      </button>
    </div>
  );
}

// ─── Empty State ───────────────────────────────────────────────────────────────
export function EmptyState({ label = "لا توجد بيانات لعرضها" }: { label?: string }) {
  return (
    <div className="flex flex-col items-center justify-center py-14 gap-3 text-center animate-fade-in">
      <div className="w-14 h-14 rounded-full bg-slate-100 dark:bg-slate-700 flex items-center justify-center">
        <InboxIcon className="w-7 h-7 text-slate-400" />
      </div>
      <p className="text-sm text-slate-400 dark:text-slate-500">{label}</p>
    </div>
  );
}

// ─── Modal ─────────────────────────────────────────────────────────────────────
interface ModalProps {
  title: string;
  onClose: () => void;
  children: React.ReactNode;
  footer?: React.ReactNode;
  maxWidth?: string;
}
export function Modal({ title, onClose, children, footer, maxWidth = "max-w-[540px]" }: ModalProps) {
  // Close on Escape
  useEffect(() => {
    const handler = (e: KeyboardEvent) => e.key === "Escape" && onClose();
    window.addEventListener("keydown", handler);
    return () => window.removeEventListener("keydown", handler);
  }, [onClose]);

  return (
    <div
      className="modal-overlay"
      onMouseDown={(e) => e.target === e.currentTarget && onClose()}
    >
      <div className={`modal-box ${maxWidth} animate-pop-in`}>
        {/* Header */}
        <div className="flex items-center justify-between px-6 py-4 border-b border-slate-200 dark:border-slate-700">
          <h2 className="text-base font-700 text-slate-800 dark:text-slate-100">{title}</h2>
          <button
            onClick={onClose}
            className="p-1.5 rounded-lg text-slate-400 hover:text-slate-600 hover:bg-slate-100 dark:hover:bg-slate-700 transition-colors"
          >
            <X className="w-4 h-4" />
          </button>
        </div>
        {/* Body */}
        <div className="px-6 py-5">{children}</div>
        {/* Footer */}
        {footer && (
          <div className="flex items-center justify-end gap-2 px-6 py-4 border-t border-slate-200 dark:border-slate-700">
            {footer}
          </div>
        )}
      </div>
    </div>
  );
}

// ─── Toast ─────────────────────────────────────────────────────────────────────
interface ToastProps {
  message: string;
  type: "success" | "error";
  onDone: () => void;
}
export function Toast({ message, type, onDone }: ToastProps) {
  useEffect(() => {
    const t = setTimeout(onDone, 3200);
    return () => clearTimeout(t);
  }, [onDone]);

  return (
    <div className="toast-wrap">
      <div
        className={`flex items-center gap-2.5 px-5 py-3 rounded-2xl text-white text-sm font-500 shadow-lg ${
          type === "success" ? "bg-emerald-500" : "bg-red-500"
        }`}
      >
        {type === "success" ? (
          <CheckCircle2 className="w-4 h-4 shrink-0" />
        ) : (
          <XCircle className="w-4 h-4 shrink-0" />
        )}
        {message}
      </div>
    </div>
  );
}

// ─── Badge ─────────────────────────────────────────────────────────────────────
type BadgeVariant = "green" | "red" | "blue" | "yellow" | "purple" | "gray";
const badgeClasses: Record<BadgeVariant, string> = {
  green:  "bg-emerald-100 text-emerald-700 dark:bg-emerald-900/40 dark:text-emerald-400",
  red:    "bg-red-100 text-red-700 dark:bg-red-900/40 dark:text-red-400",
  blue:   "bg-blue-100 text-blue-700 dark:bg-blue-900/40 dark:text-blue-400",
  yellow: "bg-amber-100 text-amber-700 dark:bg-amber-900/40 dark:text-amber-400",
  purple: "bg-purple-100 text-purple-700 dark:bg-purple-900/40 dark:text-purple-400",
  gray:   "bg-slate-100 text-slate-600 dark:bg-slate-700 dark:text-slate-300",
};
export function Badge({ variant, children }: { variant: BadgeVariant; children: React.ReactNode }) {
  return <span className={`badge ${badgeClasses[variant]}`}>{children}</span>;
}

// ─── Stat Card ─────────────────────────────────────────────────────────────────
type StatCardColor = "default" | "green" | "red" | "blue" | "yellow" | "purple" | "gray";
interface StatCardProps {
  label: string;
  value: string | number;
  icon?: React.ReactNode;
  color?: StatCardColor;
  sub?: string;
}
const statColors: Record<StatCardColor, string> = {
  default: "text-slate-800 dark:text-slate-100",
  green:   "text-emerald-600 dark:text-emerald-400",
  red:     "text-red-600 dark:text-red-400",
  blue:    "text-blue-600 dark:text-blue-400",
  yellow:  "text-amber-600 dark:text-amber-400",
  purple:  "text-purple-600 dark:text-purple-400",
  gray:    "text-slate-600 dark:text-slate-400",
};
export function StatCard({ label, value, icon, color = "default", sub }: StatCardProps) {
  return (
    <div className="stat-card-inner animate-slide-up">
      <div className="flex items-start justify-between gap-2 mb-2">
        <p className="text-xs font-600 text-slate-400 dark:text-slate-500 uppercase tracking-wide">{label}</p>
        {icon && <span className="text-slate-300 dark:text-slate-600 text-lg">{icon}</span>}
      </div>
      <p className={`text-2xl font-800 num ${statColors[color]}`}>{value}</p>
      {sub && <p className="text-xs text-slate-400 dark:text-slate-500 mt-1">{sub}</p>}
    </div>
  );
}

// ─── Form Group ───────────────────────────────────────────────────────────────
export function FormGroup({
  label, required, children, hint,
}: { label: string; required?: boolean; children: React.ReactNode; hint?: string }) {
  return (
    <div className="space-y-1.5">
      <label className="text-sm font-600 text-slate-600 dark:text-slate-300">
        {label}
        {required && <span className="text-red-500 mr-0.5">*</span>}
      </label>
      {children}
      {hint && <p className="text-xs text-slate-400 dark:text-slate-500">{hint}</p>}
    </div>
  );
}

// ─── Section Header ───────────────────────────────────────────────────────────
export function SectionHeader({
  title, count, action,
}: { title: string; count?: number; action?: React.ReactNode }) {
  return (
    <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-3 mb-5">
      <div className="flex items-center gap-2.5">
        <h2 className="text-lg font-700 text-slate-800 dark:text-slate-100">{title}</h2>
        {count !== undefined && (
          <span className="badge bg-blue-100 dark:bg-blue-900/40 text-blue-600 dark:text-blue-400 text-xs">
            {count}
          </span>
        )}
      </div>
      {action}
    </div>
  );
}

// ─── Spinner ──────────────────────────────────────────────────────────────────
export function Spinner({ size = "sm" }: { size?: "sm" | "md" }) {
  return (
    <Loader2
      className={`animate-spin-erp ${size === "sm" ? "w-4 h-4" : "w-5 h-5"}`}
    />
  );
}

// ─── Tab Bar ──────────────────────────────────────────────────────────────────
interface Tab { id: string; label: string; icon?: React.ReactNode }
export function TabBar({
  tabs, active, onChange,
}: { tabs: Tab[]; active: string; onChange: (id: string) => void }) {
  return (
    <div className="flex flex-wrap gap-1.5 mb-6 p-1 bg-slate-100 dark:bg-slate-800 rounded-2xl w-full">
      {tabs.map((t) => (
        <button
          key={t.id}
          onClick={() => onChange(t.id)}
          className={`erp-btn px-4 py-2 text-sm rounded-xl transition-all ${
            active === t.id
              ? "bg-white dark:bg-slate-700 text-blue-600 dark:text-blue-400 shadow-sm font-600"
              : "text-slate-500 dark:text-slate-400 hover:text-slate-700 dark:hover:text-slate-300"
          }`}
        >
          {t.icon && <span>{t.icon}</span>}
          {t.label}
        </button>
      ))}
    </div>
  );
}
