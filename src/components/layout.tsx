"use client";
import React from "react";
import {
  LayoutDashboard, Package, Users, Truck, ArrowRightLeft,
  Banknote, Sun, Moon, ChevronRight, Menu, X,
  FileText, LogOut,
} from "lucide-react";
import logo from "../../public/favicon.ico";

export type ModuleId = "dashboard" | "definitions" | "transactions" | "statements" | "finance";

interface NavItem { id: ModuleId; label: string; icon: React.ReactNode; group?: string; }

const NAV_ITEMS: NavItem[] = [
  { id: "dashboard",    label: "لوحة التحكم",       icon: <LayoutDashboard size={18} /> },
  { id: "definitions",  label: "التعريفات",           icon: <Package size={18} />,        group: "الوحدات" },
  { id: "transactions", label: "المعاملات",           icon: <ArrowRightLeft size={18} /> },
  { id: "statements",   label: "كشوفات الحسابات",    icon: <FileText size={18} /> },
  { id: "finance",      label: "الحسابات المالية",   icon: <Banknote size={18} /> },
];

interface SidebarProps {
  active: ModuleId;
  onNavigate: (id: ModuleId) => void;
  dark: boolean;
  onToggleDark: () => void;
  isOpen?: boolean;
  onClose?: () => void;
  onLogout?: () => void;
}

export function Sidebar({ active, onNavigate, dark, onToggleDark, isOpen, onClose, onLogout }: SidebarProps) {
  let lastGroup = "";
  return (
    <aside
      className={`fixed top-0 right-0 z-40 h-full flex min-w-0 flex-col w-[min(var(--sidebar-w),100vw)] transition-transform duration-300 ease-in-out ${
        isOpen ? "translate-x-0" : "translate-x-full"
      } lg:translate-x-0`}
      style={{
        background: "linear-gradient(180deg, #0f172a 0%, #1e293b 100%)",
        borderLeft: "1px solid rgba(255,255,255,0.06)",
      }}
    >
      {/* Logo */}
      <div className="flex items-center justify-between gap-3 px-5 py-5 border-b border-white/10">
        <div className="flex items-center gap-3">
          <div className="w-9 h-9 rounded-xl bg-gradient-to-br flex items-center justify-center shadow-lg">
            <img src={logo.src} alt="" />

          </div>
          <div>
            <p className="text-white font-700 text-sm leading-tight">ERP System</p>
            <p className="text-slate-400 text-xs mt-0.5">Dr. Ahmed Said</p>
          </div>
        </div>
        {onClose && (
          <button onClick={onClose}
            className="lg:hidden p-2 text-slate-300 hover:text-white hover:bg-white/10 rounded-xl transition-colors"
            aria-label="إغلاق الشريط الجانبي">
            <X size={18} />
          </button>
        )}
      </div>

      {/* Nav */}
      <nav className="flex-1 py-4 overflow-y-auto space-y-0.5">
        {NAV_ITEMS.map((item) => {
          const showGroup = item.group && item.group !== lastGroup;
          if (showGroup) lastGroup = item.group!;
          return (
            <React.Fragment key={item.id}>
              {showGroup && (
                <p className="px-5 pt-4 pb-1 text-[10px] font-700 text-slate-500 uppercase tracking-widest">
                  {item.group}
                </p>
              )}
              <button
                onClick={() => { onNavigate(item.id); onClose?.(); }}
                className={`sidebar-link w-full text-right ${active === item.id ? "active" : ""}`}
              >
                <span className="shrink-0">{item.icon}</span>
                <span className="flex-1">{item.label}</span>
                {active === item.id && <ChevronRight size={14} className="opacity-60 rotate-180" />}
              </button>
            </React.Fragment>
          );
        })}
      </nav>

      {/* Footer */}
      <div className="px-4 py-4 border-t border-white/10 space-y-3">
        <div className="flex items-center justify-between px-2">
          <span className="text-slate-400 text-xs font-500">
            {dark ? "🌙 الوضع الداكن" : "☀️ الوضع الفاتح"}
          </span>
          <button
            onClick={onToggleDark}
            className={`relative w-10 h-5 rounded-full transition-colors duration-300 focus:outline-none ${dark ? "bg-blue-500" : "bg-slate-600"}`}
            aria-label="تبديل الثيم"
          >
            <span className={`absolute top-0.5 w-4 h-4 bg-white rounded-full shadow transition-all duration-300 flex items-center justify-center ${dark ? "right-0.5" : "left-0.5"}`}>
              {dark ? <Moon size={9} className="text-blue-500" /> : <Sun size={9} className="text-amber-500" />}
            </span>
          </button>
        </div>
        <div className="flex items-center gap-2 px-2">
          <div className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse" />
          <span className="text-slate-500 text-xs">متصل بـ Google Sheets</span>
        </div>
        {onLogout && (
          <button
            onClick={onLogout}
            className="w-full flex items-center gap-2 px-3 py-2 rounded-lg text-slate-400 hover:text-red-400 hover:bg-red-400/10 transition-colors text-sm"
          >
            <LogOut size={14} />
            <span>تسجيل الخروج</span>
          </button>
        )}
      </div>
    </aside>
  );
}

// ─── Top Bar ────────────────────────────────────────────────────────────────────
const MODULE_TITLES: Record<ModuleId, { label: string; icon: React.ReactNode }> = {
  dashboard:    { label: "لوحة التحكم الرئيسية",            icon: <LayoutDashboard size={18} /> },
  definitions:  { label: "التعريفات والبيانات الأساسية",    icon: <Package size={18} /> },
  transactions: { label: "المعاملات — مشتريات ومبيعات",     icon: <ArrowRightLeft size={18} /> },
  statements:   { label: "كشوفات حسابات الموردين والعملاء", icon: <FileText size={18} /> },
  finance:      { label: "الحسابات المالية",                 icon: <Banknote size={18} /> },
};

export function TopBar({ active, onToggleSidebar }: { active: ModuleId; onToggleSidebar?: () => void }) {
  const now = new Date().toLocaleDateString("ar-EG", {
    weekday: "long", year: "numeric", month: "long", day: "numeric",
  });
  const meta = MODULE_TITLES[active];
  return (
    <header
      className="sticky top-0 z-30 flex items-center justify-between px-4 sm:px-6"
      style={{
        height: "var(--topbar-h)",
        borderBottom: "1px solid",
        borderColor: "rgba(0,0,0,0.06)",
        background: "rgba(255,255,255,0.9)",
        backdropFilter: "blur(12px)",
      }}
    >
      <div className="flex items-center gap-2.5 text-slate-700">
        {onToggleSidebar && (
          <button onClick={onToggleSidebar}
            className="lg:hidden p-2 rounded-xl text-slate-600 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
            aria-label="فتح الشريط الجانبي">
            <Menu size={18} />
          </button>
        )}
        {meta.icon}
        <h1 className="text-base font-700">{meta.label}</h1>
      </div>
      <p className="text-sm text-slate-400 font-500 hidden sm:block">{now}</p>
    </header>
  );
}
