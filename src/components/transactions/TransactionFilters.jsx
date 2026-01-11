export default function TransactionFilters({ filters, setFilters, onApply }) {

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFilters(prev => ({
      ...prev,
      [name]: value
    }));
  };

  return (
    <div className="filter-bar">

      <input
        name="senderAccount"
        placeholder="Sender Account"
        value={filters.senderAccount}
        onChange={handleChange}
      />

      <input
        name="receiverAccount"
        placeholder="Receiver Account"
        value={filters.receiverAccount}
        onChange={handleChange}
      />

      <select
        name="fraudStatus"
        value={filters.fraudStatus}
        onChange={handleChange}
      >
        <option value="">All Fraud Status</option>
        <option value="OK">OK</option>
        <option value="SUSPICIOUS">SUSPICIOUS</option>
        <option value="FRAUD">FRAUD</option>
      </select>

      <select
        name="status"
        value={filters.status}
        onChange={handleChange}
      >
        <option value="">All Status</option>
        <option value="SUCCESS">SUCCESS</option>
        <option value="FAILED">FAILED</option>
        <option value="PENDING">PENDING</option>
      </select>

      <button onClick={onApply}>
        Apply
      </button>

    </div>
  );
}
