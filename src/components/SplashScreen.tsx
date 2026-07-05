"use client";
import React, { useEffect, useState } from "react";

export default function SplashScreen() {
  const [dot, setDot] = useState(0);

  useEffect(() => {
    const t = setInterval(() => setDot(d => (d + 1) % 3), 500);
    return () => clearInterval(t);
  }, []);

  return (
    <div
      className="fixed inset-0 z-[9999] flex flex-col items-center justify-center"
      style={{ background: "linear-gradient(135deg, #0f172a 0%, #1e3a5f 60%, #0f172a 100%)" }}
    >
      {/* Animated building icon */}
      <div className="relative mb-8">
        {/* Outer glow ring */}
        <div
          className="absolute inset-0 rounded-3xl opacity-30"
          style={{
            background: "radial-gradient(circle, #3b82f6 0%, transparent 70%)",
            transform: "scale(1.8)",
            animation: "splash-pulse 2s ease-in-out infinite",
          }}
        />
        {/* Icon box */}
        <div
          className="relative w-24 h-24 rounded-3xl flex items-center justify-center"
          style={{
            background: "linear-gradient(135deg, #2563eb, #1d4ed8)",
            boxShadow: "0 20px 60px rgba(37,99,235,0.5)",
            animation: "splash-float 3s ease-in-out infinite",
          }}
        >
          {/* Building SVG (matches the PWA icon) */}
          <svg width="52" height="52" viewBox="0 0 52 52" fill="none">
            <rect x="6" y="16" width="40" height="32" rx="3" fill="white" opacity="0.15" />
            <rect x="8" y="18" width="36" height="30" rx="2" fill="white" opacity="0.9" />
            <polygon points="26,4 4,18 48,18" fill="white" opacity="0.95" />
            <rect x="13" y="24" width="8" height="6" rx="1.5" fill="#2563eb" opacity="0.65" />
            <rect x="22" y="24" width="8" height="6" rx="1.5" fill="#2563eb" opacity="0.65" />
            <rect x="31" y="24" width="8" height="6" rx="1.5" fill="#2563eb" opacity="0.65" />
            <rect x="13" y="33" width="8" height="6" rx="1.5" fill="#2563eb" opacity="0.65" />
            <rect x="22" y="33" width="8" height="6" rx="1.5" fill="#2563eb" opacity="0.65" />
            <rect x="31" y="33" width="8" height="6" rx="1.5" fill="#2563eb" opacity="0.65" />
            <rect x="19" y="39" width="14" height="9" rx="1.5" fill="#2563eb" opacity="0.75" />
          </svg>
        </div>
      </div>

      {/* Title */}
      <h1
        className="text-white text-3xl font-800 tracking-wide mb-2"
        style={{ animation: "splash-fadein 0.8s ease both" }}
      >
        ERP System
      </h1>
      <p
        className="text-blue-300 text-sm font-500 mb-12"
        style={{ animation: "splash-fadein 0.8s ease 0.2s both" }}
      >
        Dr. Ahmed Said
      </p>

      {/* Loading dots */}
      <div className="flex items-center gap-2">
        {[0, 1, 2].map(i => (
          <div
            key={i}
            className="w-2 h-2 rounded-full"
            style={{
              background: dot === i ? "#3b82f6" : "rgba(255,255,255,0.2)",
              transform: dot === i ? "scale(1.4)" : "scale(1)",
              transition: "all 0.25s ease",
            }}
          />
        ))}
      </div>

      {/* Bottom bar */}
      <div className="absolute bottom-8 flex flex-col items-center gap-1">
        <div className="w-32 h-0.5 rounded-full bg-white/10 overflow-hidden">
          <div
            className="h-full bg-blue-400 rounded-full"
            style={{ animation: "splash-progress 1.8s ease-in-out infinite" }}
          />
        </div>
        <p className="text-white/30 text-xs mt-2">جارٍ التحقق من الجلسة...</p>
      </div>

      <style>{`
        @keyframes splash-float {
          0%, 100% { transform: translateY(0px); }
          50%       { transform: translateY(-8px); }
        }
        @keyframes splash-pulse {
          0%, 100% { opacity: 0.15; transform: scale(1.6); }
          50%       { opacity: 0.35; transform: scale(2.0); }
        }
        @keyframes splash-fadein {
          from { opacity: 0; transform: translateY(12px); }
          to   { opacity: 1; transform: translateY(0); }
        }
        @keyframes splash-progress {
          0%   { width: 0%; margin-left: 0; }
          50%  { width: 70%; margin-left: 0; }
          100% { width: 0%; margin-left: 100%; }
        }
      `}</style>
    </div>
  );
}
