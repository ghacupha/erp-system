"use client";
import React, { useEffect, useState } from "react";
import { Download, Share, X } from "lucide-react";

interface BeforeInstallPromptEvent extends Event {
  prompt: () => Promise<void>;
  userChoice: Promise<{ outcome: "accepted" | "dismissed" }>;
}

const STORAGE_KEY = "pwa-install-dismissed";

export default function InstallPrompt() {
  const [show,     setShow]     = useState(false);
  const [isIos,    setIsIos]    = useState(false);
  const [deferred, setDeferred] = useState<BeforeInstallPromptEvent | null>(null);

  useEffect(() => {
    if (typeof window === "undefined") return;

    // Never show if running as installed PWA
    if (window.matchMedia("(display-mode: standalone)").matches) return;

    // Never show if user permanently dismissed
    if (localStorage.getItem(STORAGE_KEY)) return;

    const ios = /iphone|ipad|ipod/i.test(navigator.userAgent) && !(window as any).MSStream;
    setIsIos(ios);

    if (ios) {
      // iOS Safari: show instructions immediately (no beforeinstallprompt on iOS)
      const isSafari = /^((?!chrome|android).)*safari/i.test(navigator.userAgent);
      if (isSafari) setShow(true);          // show right away, no delay
      return;
    }

    // Android / Chrome: capture the native prompt event
    const handler = (e: Event) => {
      e.preventDefault();
      setDeferred(e as BeforeInstallPromptEvent);
      setShow(true);                         // show immediately when event fires
    };
    window.addEventListener("beforeinstallprompt", handler);
    return () => window.removeEventListener("beforeinstallprompt", handler);
  }, []);

  const dismiss = (permanent: boolean) => {
    if (permanent) localStorage.setItem(STORAGE_KEY, "1");
    setShow(false);
  };

  const install = async () => {
    if (!deferred) return;
    await deferred.prompt();
    const { outcome } = await deferred.userChoice;
    if (outcome === "accepted") {
      localStorage.setItem(STORAGE_KEY, "1"); // installed — never show again
      setShow(false);
    }
    setDeferred(null);
  };

  if (!show) return null;

  return (
    // Full-screen backdrop — user must respond before continuing
    <div className="fixed inset-0 z-[9999] flex items-end sm:items-center justify-center p-4"
         style={{ background: "rgba(0,0,0,0.55)", backdropFilter: "blur(4px)" }}>
      <div
        className="w-full max-w-sm rounded-2xl overflow-hidden"
        style={{ boxShadow: "0 24px 64px rgba(0,0,0,0.4)" }}
      >
        {/* Header strip */}
        <div style={{ background: "linear-gradient(135deg,#1e40af,#2563eb)" }}
             className="px-5 py-4 flex items-center gap-4">
          <img src="/icons/icon-192.png" alt="ERP" width={52} height={52}
               className="rounded-2xl shadow-lg shrink-0"
               onError={e => { (e.target as HTMLImageElement).style.display = "none"; }} />
          <div className="flex-1 min-w-0">
            <p className="text-white font-700 text-base leading-tight">ERP System</p>
            <p className="text-blue-200 text-xs mt-0.5">أضفه للشاشة الرئيسية لوصول أسرع</p>
          </div>
          <button onClick={() => dismiss(false)} aria-label="لاحقاً"
                  className="p-1.5 rounded-xl text-white/60 hover:text-white hover:bg-white/10 transition-colors shrink-0">
            <X size={18} />
          </button>
        </div>

        {/* Body */}
        <div className="bg-white dark:bg-slate-800 p-5">
          {isIos ? (
            <div className="space-y-3">
              <p className="text-sm font-600 text-slate-700 dark:text-slate-200">
                خطوات التثبيت على iPhone / iPad:
              </p>
              {[
                { n: "١", text: <>اضغط زر <Share size={14} className="inline text-blue-500 -mt-0.5" /> المشاركة في أسفل Safari</> },
                { n: "٢", text: <>اختر <strong>"إضافة إلى الشاشة الرئيسية"</strong></> },
                { n: "٣", text: <>اضغط <strong>"إضافة"</strong> للتأكيد</> },
              ].map(({ n, text }) => (
                <div key={n} className="flex items-center gap-3 text-sm text-slate-600 dark:text-slate-300">
                  <span className="w-7 h-7 rounded-full bg-blue-100 dark:bg-blue-900/40 text-blue-600 dark:text-blue-400 text-sm font-700 flex items-center justify-center shrink-0">
                    {n}
                  </span>
                  <span>{text}</span>
                </div>
              ))}
              <div className="flex gap-2 pt-2">
                <button onClick={() => dismiss(false)}
                        className="erp-btn flex-1 justify-center border border-slate-200 dark:border-slate-600 text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">
                  لاحقاً
                </button>
                <button onClick={() => dismiss(true)}
                        className="erp-btn flex-1 justify-center bg-blue-500 hover:bg-blue-600 text-white">
                  فهمت ✓
                </button>
              </div>
            </div>
          ) : (
            <div className="space-y-4">
              <p className="text-sm text-slate-600 dark:text-slate-300">
                ثبّت التطبيق على هاتفك للوصول إليه مثل أي تطبيق — بدون فتح المتصفح وبأيقونة مخصصة.
              </p>
              <div className="flex gap-2">
                <button onClick={() => dismiss(true)}
                        className="erp-btn flex-1 justify-center border border-slate-200 dark:border-slate-600 text-slate-500 hover:bg-slate-50 dark:hover:bg-slate-700">
                  لا شكراً
                </button>
                <button onClick={install}
                        className="erp-btn flex-1 justify-center bg-blue-500 hover:bg-blue-600 text-white gap-1.5">
                  <Download size={16} /> تثبيت الآن
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
