import pandas as pd
import statsmodels.api as sm
from sklearn.cluster import KMeans
import matplotlib.pyplot as plt
import seaborn as sns

df = pd.DataFrame(pd.read_csv('../sa_result_test_tours_associated_to_fitness.csv', sep=',', header=0))

df['Temperature'] = pd.to_numeric(df['Temperature'], errors='coerce')
df['Cooling Rate'] = pd.to_numeric(df['Cooling Rate'], errors='coerce')
df['Max Temperature Change'] = pd.to_numeric(df['Max Temperature Change'], errors='coerce')
df['Fitness'] = pd.to_numeric(df['Fitness'], errors='coerce')

df = df.dropna() # drop les valeurs null

X = df[['Temperature', 'Cooling Rate', 'Max Temperature Change']]
y = df['Fitness']

X = sm.add_constant(X)
model = sm.OLS(y, X).fit()

print(model.summary())


# Clustering avec kmeans
kmeans = KMeans(n_clusters=10, random_state=0).fit(df[['Temperature', 'Cooling Rate', 'Max Temperature Change', 'Fitness']])
df['Cluster'] = kmeans.labels_
cluster_stats = df.groupby('Cluster').agg(['mean', 'std', 'min', 'max'])
print(cluster_stats)

# Visualisation des intervalles par cluster
plt.figure(figsize=(14, 8))

# Boxplot pour 'Cooling Rate'
plt.subplot(2, 2, 1)
sns.boxplot(x='Cluster', y='Cooling Rate', data=df)
plt.title('Cooling Rate by Cluster')

# Boxplot pour 'Temperature'
plt.subplot(2, 2, 2)
sns.boxplot(x='Cluster', y='Temperature', data=df)
plt.title('Temperature by Cluster')

# Boxplot pour 'Max Temperature Change'
plt.subplot(2, 2, 3)
sns.boxplot(x='Cluster', y='Max Temperature Change', data=df)
plt.title('Max Temperature Change by Cluster')

# Boxplot pour 'Fitness'
plt.subplot(2, 2, 4)
sns.boxplot(x='Cluster', y='Fitness', data=df)
plt.title('Fitness by Cluster')

plt.tight_layout()
plt.show()