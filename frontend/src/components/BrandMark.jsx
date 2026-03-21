export default function BrandMark() {
  return (
    <div className="flex items-center gap-3">
      <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-[linear-gradient(135deg,#f27121,#c2410c)] text-lg font-bold text-white shadow-lg shadow-orange-900/20 ring-1 ring-white/20">
        TB
      </div>
      <div>
        <h1 className="text-base font-bold leading-tight text-ink md:text-lg">Truck Bazaar</h1>
        <p className="mt-1 text-[11px] tracking-[0.14em] text-steel">Kushal Mangal Transport Service</p>
      </div>
    </div>
  );
}
